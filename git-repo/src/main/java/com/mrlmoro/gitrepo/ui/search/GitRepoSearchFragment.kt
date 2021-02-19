package com.mrlmoro.gitrepo.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mrlmoro.core.ui.BaseFragment
import com.mrlmoro.core.ui.extensions.hideKeyboard
import com.mrlmoro.core.ui.extensions.showKeyboard
import com.mrlmoro.core.ui.extensions.showSnackbar
import com.mrlmoro.gitrepo.R
import com.mrlmoro.gitrepo.databinding.FragmentGitRepoSearchBinding
import com.mrlmoro.gitrepo.ui.detail.GitRepoDetailDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class GitRepoSearchFragment : BaseFragment<FragmentGitRepoSearchBinding>() {

    private val viewModel: GitRepoSearchViewModel by viewModel()

    private val gitRepoSearchAdapter = GitRepoSearchAdapter()

    private var snackbarError: Snackbar? = null

    override fun inflate(inflater: LayoutInflater) = FragmentGitRepoSearchBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        setupAdapter()
        setupSearch()
        setupClear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        snackbarError?.dismiss()
    }

    private fun observeData() {
        viewModel.repositories.observe(viewLifecycleOwner, {
            gitRepoSearchAdapter.submitList(it)
            showList()
        })

        viewModel.event.observe(viewLifecycleOwner, {
            when (it) {
                is GitRepoSearchViewModel.Event.Loading -> showLoading()
                is GitRepoSearchViewModel.Event.NotFound -> showNotFound()
                is GitRepoSearchViewModel.Event.Error -> showError()
            }
        })
    }

    private fun setupAdapter() {
        binding.rvRepositories.layoutManager = LinearLayoutManager(context)
        binding.rvRepositories.adapter = gitRepoSearchAdapter
        gitRepoSearchAdapter.listener = {
            GitRepoDetailDialogFragment(it)
                .show(childFragmentManager, null)
        }
    }

    private fun setupSearch() {
        binding.viewSearch.etSearch.requestFocus()
        view?.showKeyboard()

        binding.viewSearch.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) search()
            return@setOnEditorActionListener false
        }
    }

    private fun setupClear() {
        binding.viewSearch.ivClear.setOnClickListener {
            binding.viewSearch.etSearch.setText("")
        }
    }

    private fun search() {
        view?.hideKeyboard()
        viewModel.search(binding.viewSearch.etSearch.text.toString())
    }

    private fun showLoading() {
        binding.progress.visibility = View.VISIBLE
        binding.rvRepositories.visibility = View.GONE
    }

    private fun showNotFound() {
        binding.progress.visibility = View.GONE
        binding.root.showSnackbar(R.string.repository_not_found)
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
            listener = { search() }
        )
    }

}