package com.mrlmoro.gitrepo.ui.list

import androidx.paging.DataSource
import com.mrlmoro.core.ui.PagingEvent
import com.mrlmoro.core.ui.SingleLiveData
import com.mrlmoro.gitrepo.domain.model.Repository
import com.mrlmoro.gitrepo.domain.repository.GitRepoRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable

class GitRepoListPagingDataSourceFactory(
    private val gitRepoRepository: GitRepoRepository,
    private val pagingEvent: SingleLiveData<PagingEvent>,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Long, Repository>() {

    override fun create() = GitRepoListPagingDataSource(
        gitRepoRepository,
        pagingEvent,
        compositeDisposable
    )

}