package com.mrlmoro.gitrepo.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mrlmoro.gitrepo.databinding.ItemGitRepoBinding
import com.mrlmoro.gitrepo.domain.model.Repository
import com.mrlmoro.gitrepo.ui.list.GitRepoListViewHolder

class GitRepoSearchAdapter : RecyclerView.Adapter<GitRepoListViewHolder>() {

    private val items = mutableListOf<Repository>()

    var listener: ((Repository) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitRepoListViewHolder {
        val inflate = ItemGitRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GitRepoListViewHolder(inflate, listener)
    }

    override fun onBindViewHolder(holder: GitRepoListViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun submitList(items: List<Repository>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }
}