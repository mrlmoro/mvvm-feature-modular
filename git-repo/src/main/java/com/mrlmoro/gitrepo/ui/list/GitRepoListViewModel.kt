package com.mrlmoro.gitrepo.ui.list

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.mrlmoro.core.ui.BaseViewModel
import com.mrlmoro.core.ui.PagingEvent
import com.mrlmoro.core.ui.SingleLiveData
import com.mrlmoro.gitrepo.domain.model.Repository
import com.mrlmoro.gitrepo.domain.repository.GitRepoRepository

class GitRepoListViewModel(gitRepoRepository: GitRepoRepository) : BaseViewModel() {

    val pagingEvent = SingleLiveData<PagingEvent>()

    val repositories: LiveData<PagedList<Repository>>

    init {
        val factory = GitRepoListPagingDataSourceFactory(
            gitRepoRepository,
            pagingEvent,
            compositeDisposable
        )
        val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setInitialLoadSizeHint(PAGE_SIZE)
            .setEnablePlaceholders(false)
            .build()
        repositories = LivePagedListBuilder(factory, config).build()
    }

    fun retry() {
        repositories.value?.dataSource?.invalidate()
    }

    private companion object {
        const val PAGE_SIZE = 20
    }

}