package com.mrlmoro.gitrepo.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.mrlmoro.gitrepo.Mocks
import com.mrlmoro.gitrepo.domain.model.Repository
import com.mrlmoro.gitrepo.domain.repository.GitRepoRepository
import com.mrlmoro.gitrepo.ui.search.GitRepoSearchViewModel
import io.mockk.*
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GitRepoSearchViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val gitRepoRepositoryMock = mockkClass(GitRepoRepository::class)

    private val repositoriesObserverMock: Observer<List<Repository>> = mockk(relaxed = true)

    private val eventObserverMock: Observer<GitRepoSearchViewModel.Event> = mockk(relaxed = true)

    private lateinit var gitRepoSearchViewModel: GitRepoSearchViewModel

    @Before
    fun `Setup test`() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        gitRepoSearchViewModel = GitRepoSearchViewModel(gitRepoRepositoryMock)
        gitRepoSearchViewModel.repositories.observeForever(repositoriesObserverMock)
        gitRepoSearchViewModel.event.observeForever(eventObserverMock)
    }

    @Test
    fun `Test search() when success`() {
        //Prepare
        val mocks = listOf(Mocks.repositoryMock)
        mockSearchRepositories(Single.just(mocks))

        //Action
        gitRepoSearchViewModel.search("mock")

        //Test
        verifyOrder {
            eventObserverMock.onChanged(GitRepoSearchViewModel.Event.Loading)
            repositoriesObserverMock.onChanged(mocks)
        }
    }

    @Test
    fun `Test search() when not found`() {
        //Prepare
        val mocks = emptyList<Repository>()
        mockSearchRepositories(Single.just(mocks))

        //Action
        gitRepoSearchViewModel.search("mock")

        //Test
        verifyOrder {
            eventObserverMock.onChanged(GitRepoSearchViewModel.Event.Loading)
            repositoriesObserverMock.onChanged(mocks)
            eventObserverMock.onChanged(GitRepoSearchViewModel.Event.NotFound)
        }
    }

    @Test
    fun `Test search() when error`() {
        //Prepare
        mockSearchRepositories(Single.error(NullPointerException()))

        //Action
        gitRepoSearchViewModel.search("mock")

        //Test
        verifyOrder {
            eventObserverMock.onChanged(GitRepoSearchViewModel.Event.Loading)
            eventObserverMock.onChanged(GitRepoSearchViewModel.Event.Error)
        }
        verify { repositoriesObserverMock wasNot Called }
    }

    //Helpers

    private fun mockSearchRepositories(single: Single<List<Repository>>) {
        every {
            gitRepoRepositoryMock.searchRepositories(
                query = "mock",
                pageSize = GitRepoSearchViewModel.PAGE_SIZE
            )
        } returns single
    }
}