package com.mrlmoro.gitrepo.ui.list

import androidx.paging.PageKeyedDataSource
import com.mrlmoro.core.ui.PagingEvent
import com.mrlmoro.core.ui.SingleLiveData
import com.mrlmoro.gitrepo.domain.model.Repository
import com.mrlmoro.gitrepo.domain.repository.GitRepoRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class GitRepoListPagingDataSource(
    private val gitRepoRepository: GitRepoRepository,
    private val pagingEvent: SingleLiveData<PagingEvent>,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Long, Repository>() {

    override fun loadInitial(
        params: LoadInitialParams<Long>,
        callback: LoadInitialCallback<Long, Repository>
    ) {
        pagingEvent.postValue(PagingEvent.Loading)
        fetch(
            pageSize = params.requestedLoadSize,
            onSuccess = {
                pagingEvent.postValue(PagingEvent.Success)
                callback.onResult(it, null, it.last().id)
            },
            onError = {
                pagingEvent.postValue(PagingEvent.Error)
            }
        )
    }

    override fun loadAfter(
        params: LoadParams<Long>,
        callback: LoadCallback<Long, Repository>
    ) {
        fetch(
            since = params.key,
            pageSize = params.requestedLoadSize,
            onSuccess = { callback.onResult(it, it.last().id) }
        )
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, Repository>) {}

    private fun fetch(
        since: Long? = null,
        pageSize: Int,
        onSuccess: (List<Repository>) -> Unit,
        onError: ((Throwable) -> Unit)? = null
    ) {
        gitRepoRepository.getRepositories(since)
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    val list = if (it.size > pageSize) {
                        it.subList(0, pageSize)
                    } else {
                        it.subList(0, it.size)
                    }
                    onSuccess(list)
                },
                { onError?.invoke(it) }
            )
            .also { compositeDisposable.add(it) }
    }
}