package com.mrlmoro.gitrepo.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mrlmoro.core.ui.BaseFragment
import com.mrlmoro.core.ui.PagingEvent
import com.mrlmoro.core.ui.extensions.showSnackbar
import com.mrlmoro.gitrepo.R
import com.mrlmoro.gitrepo.databinding.FragmentGitRepoListBinding
import com.mrlmoro.gitrepo.ui.detail.GitRepoDetailDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class GitRepoListFragment : BaseFragment<FragmentGitRepoListBinding>() {

    private val viewModel: GitRepoListViewModel by viewModel()

    private val gitRepoListAdapter = GitRepoListAdapter()

    private var snackbarError: Snackbar? = null

    override fun inflate(inflater: LayoutInflater) = FragmentGitRepoListBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        setupAdapter()
        viewModel.retry()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        snackbarError?.dismiss()
    }

    private fun observeData() {
        viewModel.repositories.observe(viewLifecycleOwner, {
            gitRepoListAdapter.submitList(it)
        })

        viewModel.pagingEvent.observe(viewLifecycleOwner, {
            when (it) {
                is PagingEvent.Loading -> showLoading()
                is PagingEvent.Success -> showList()
                is PagingEvent.Error -> showError()
            }
        })
    }

    private fun setupAdapter() {
        binding.rvRepositories.layoutManager = LinearLayoutManager(context)
        binding.rvRepositories.adapter = gitRepoListAdapter
        gitRepoListAdapter.listener = {
            GitRepoDetailDialogFragment(it)
                .show(childFragmentManager, null)
        }
    }

    private fun showLoading() {
        binding.progress.visibility = View.VISIBLE
        binding.rvRepositories.visibility = View.GONE
    }

    private fun showList() {
        binding.progress.visibility = View.GONE
        binding.rvRepositories.visibility = View.VISIBLE
    }

    private fun showError() {
        binding.progress.visibility = View.GONE
        snackbarError = binding.root.showSnackbar(
            resId = R.string.unexpected_error,
            duration = Snackbar.LENGTH_INDEFINITE,
            hasBottomNav = true,
            listener = { viewModel.retry() }
        )
    }

}