package com.mrlmoro.gitrepo.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mrlmoro.gitrepo.databinding.ItemGitRepoBinding
import com.mrlmoro.gitrepo.domain.model.Repository

class GitRepoListAdapter : PagedListAdapter<Repository, RecyclerView.ViewHolder>(diffCallback) {

    var listener: ((Repository) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflate = ItemGitRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GitRepoListViewHolder(inflate, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is GitRepoListViewHolder) {
            getItem(position)?.let { holder.bind(it) }
        }
    }

    private companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Repository>() {
            override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean {
                return oldItem == newItem
            }
        }
    }

}