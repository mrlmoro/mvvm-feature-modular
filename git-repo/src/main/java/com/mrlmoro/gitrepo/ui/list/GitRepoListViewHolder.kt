package com.mrlmoro.gitrepo.ui.list

import androidx.recyclerview.widget.RecyclerView
import com.mrlmoro.gitrepo.databinding.ItemGitRepoBinding
import com.mrlmoro.gitrepo.domain.model.Repository

class GitRepoListViewHolder(
    private val binding: ItemGitRepoBinding,
    private val listener: ((Repository) -> Unit)?
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(repository: Repository) {
        binding.tvName.text = repository.owner.username
            .plus("/")
            .plus(repository.name)

        binding.clContainer.setOnClickListener { listener?.invoke(repository) }
    }

}