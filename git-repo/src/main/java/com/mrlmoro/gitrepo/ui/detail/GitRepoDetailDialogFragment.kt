package com.mrlmoro.gitrepo.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mrlmoro.core.ui.extensions.loadImage
import com.mrlmoro.gitrepo.R
import com.mrlmoro.gitrepo.databinding.DialogGitRepoDetailBinding
import com.mrlmoro.gitrepo.domain.model.Repository

class GitRepoDetailDialogFragment(
    private val repository: Repository
) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogGitRepoDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogGitRepoDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            dismiss()
            return
        }

        binding.ivAvatar.loadImage(repository.owner.avatarUrl)

        binding.tvUsername.text = getString(
            R.string.git_repo_detail_username,
            repository.owner.username
        )

        binding.tvRepoName.text = getString(
            R.string.git_repo_detail_repository,
            repository.name
        )

        binding.tvRepoDescription.text = repository.description
    }
}