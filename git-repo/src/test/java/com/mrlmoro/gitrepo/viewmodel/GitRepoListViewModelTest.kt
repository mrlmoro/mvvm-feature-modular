package com.mrlmoro.gitrepo.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.mrlmoro.gitrepo.Mocks.repositoryMock
import com.mrlmoro.gitrepo.domain.model.Repository
import com.mrlmoro.gitrepo.domain.repository.GitRepoRepository
import com.mrlmoro.gitrepo.ui.list.GitRepoListViewModel
import com.mrlmoro.core.ui.PagingEvent
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import io.mockk.verifyOrder
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GitRepoListViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val gitRepoRepositoryMock = mockkClass(GitRepoRepository::class)

    private val repositoriesObserverMock: Observer<PagedList<Repository>> = mockk(relaxed = true)

    private val pagingEventObserverMock: Observer<PagingEvent> = mockk(relaxed = true)

    private lateinit var gitRepoListViewModel: GitRepoListViewModel

    @Before
    fun `Setup test`() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        gitRepoListViewModel = GitRepoListViewModel(gitRepoRepositoryMock)
        gitRepoListViewModel.pagingEvent.observeForever(pagingEventObserverMock)
    }

    @Test
    fun `Test initial repositories load page when success`() {
        //Prepare
        val mocks = (1..20).map { repositoryMock.copy(id = it.toLong()) }
        every { gitRepoRepositoryMock.getRepositories() } returns Single.just(mocks)

        //Action
        val result = repositoriesPagingResult()

        //Test
        assertTrue(result.containsAll(mocks))
        assertTrue(result.size == 20)
        assertTrue(result.last() == mocks.last())
        verifyOrder {
            pagingEventObserverMock.onChanged(PagingEvent.Loading)
            pagingEventObserverMock.onChanged(PagingEvent.Success)
        }
    }

    @Test
    fun `Test initial repositories load page when error`() {
        //Prepare
        every {
            gitRepoRepositoryMock.getRepositories()
        } returns Single.error(NullPointerException())

        //Action
        val result = repositoriesPagingResult()

        //Test
        assertTrue(result.isEmpty())
        verifyOrder {
            pagingEventObserverMock.onChanged(PagingEvent.Loading)
            pagingEventObserverMock.onChanged(PagingEvent.Error)
        }
    }

    @Test
    fun `Test next repositories load page when success`() {
        //Prepare
        val mocks = (1..20).map { repositoryMock.copy(id = it.toLong()) }
        every { gitRepoRepositoryMock.getRepositories() } returns Single.just(mocks)

        val nextPageMocks = (21..40).map { repositoryMock.copy(id = it.toLong()) }
        every {
            gitRepoRepositoryMock.getRepositories(since = 20)
        } returns Single.just(nextPageMocks)

        repositoriesPagingResult()

        //Action
        val result = repositoriesNextPagingResult()

        //Test
        assertTrue(result.containsAll(mocks))
        assertTrue(result.containsAll(nextPageMocks))
        assertTrue(result.size == 40)
        assertTrue(result.last() == nextPageMocks.last())
    }

    @Test
    fun `Test next repositories load page when error`() {
        //Prepare
        val mocks = (1..20).map { repositoryMock.copy(id = it.toLong()) }
        every { gitRepoRepositoryMock.getRepositories() } returns Single.just(mocks)

        every {
            gitRepoRepositoryMock.getRepositories(since = 20)
        } returns Single.error(NullPointerException())

        repositoriesPagingResult()

        //Action
        val result = repositoriesNextPagingResult()

        //Test
        assertTrue(result.containsAll(mocks))
        assertTrue(result.size == 20)
        assertTrue(result.last() == mocks.last())
    }

    //Helpers

    private fun repositoriesPagingResult(): List<Repository> {
        gitRepoListViewModel.repositories.observeForever(repositoriesObserverMock)
        return gitRepoListViewModel.repositories.value!!.toList()
    }

    private fun repositoriesNextPagingResult(): List<Repository> {
        gitRepoListViewModel.repositories.value!!.loadAround(19)
        return gitRepoListViewModel.repositories.value!!.toList()
    }

}