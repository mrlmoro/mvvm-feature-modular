package com.mrlmoro.gitrepo.ui.search

import androidx.lifecycle.MutableLiveData
import com.mrlmoro.core.ui.BaseViewModel
import com.mrlmoro.core.ui.SingleLiveData
import com.mrlmoro.gitrepo.domain.model.Repository
import com.mrlmoro.gitrepo.domain.repository.GitRepoRepository
import io.reactivex.rxjava3.disposables.Disposable

class GitRepoSearchViewModel(
    private val gitRepoRepository: GitRepoRepository
) : BaseViewModel() {

    val event = SingleLiveData<Event>()

    val repositories = MutableLiveData<List<Repository>>()

    private var searchDisposable: Disposable? = null

    fun search(query: String) {
        event.value = Event.Loading
        searchDisposable?.dispose()
        searchDisposable = subscribeSingle(
            observable = gitRepoRepository.searchRepositories(
                query = query,
                pageSize = PAGE_SIZE
            ),
            success = {
                repositories.value = it
                if (it.isEmpty()) {
                    event.value = Event.NotFound
                }
            },
            error = {
                event.value = Event.Error
            }
        )
    }

    sealed class Event {
        object Loading : Event()
        object NotFound : Event()
        object Error : Event()
    }

    companion object {
        const val PAGE_SIZE = 50
    }
}