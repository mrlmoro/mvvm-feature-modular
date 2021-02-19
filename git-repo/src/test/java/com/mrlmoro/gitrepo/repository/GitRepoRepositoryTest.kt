package com.mrlmoro.gitrepo.repository

import com.mrlmoro.gitrepo.Mocks.repositoryDtoResponseMock
import com.mrlmoro.gitrepo.Mocks.repositoryMock
import com.mrlmoro.gitrepo.data.GitRepoApi
import com.mrlmoro.gitrepo.data.dto.PagingDto
import com.mrlmoro.gitrepo.data.repository.GitRepoRepositoryImp
import com.mrlmoro.gitrepo.domain.repository.GitRepoRepository
import io.mockk.every
import io.mockk.mockkClass
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test

class GitRepoRepositoryTest {

    private val gitRepoApiMock = mockkClass(GitRepoApi::class)

    private lateinit var gitRepoRepository: GitRepoRepository

    @Before
    fun `Setup test`() {
        gitRepoRepository = GitRepoRepositoryImp(gitRepoApiMock)
    }

    @Test
    fun `Test getRepositories() when success`() {
        //Prepare
        every {
            gitRepoApiMock.getRepositories(10)
        } returns Single.just(listOf(repositoryDtoResponseMock))

        //Action
        val testObserver = gitRepoRepository
            .getRepositories(10)
            .test()

        //Test
        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValue(listOf(repositoryMock))
    }

    @Test
    fun `Test getRepositories() when error`() {
        //Prepare
        val exceptionMock = NullPointerException()
        every { gitRepoApiMock.getRepositories() } returns Single.error(exceptionMock)

        //Action
        val testObserver = gitRepoRepository
            .getRepositories()
            .test()

        //Test
        testObserver.assertNotComplete()
        testObserver.assertError(exceptionMock)
    }

    @Test
    fun `Test searchRepositories() when success`() {
        //Prepare
        every {
            gitRepoApiMock.searchRepositories(
                query = "mock",
                page = 1,
                pageSize = 50
            )
        } returns Single.just(
            PagingDto(
                totalCount = 1,
                items = listOf(repositoryDtoResponseMock)
            )
        )

        //Action
        val testObserver = gitRepoRepository
            .searchRepositories(
                query = "mock",
                page = 1,
                pageSize = 50
            )
            .test()

        //Test
        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValue(listOf(repositoryMock))
    }

    @Test
    fun `Test searchRepositories() when error`() {
        //Prepare
        val exceptionMock = NullPointerException()
        every {
            gitRepoApiMock.searchRepositories("mock")
        } returns Single.error(exceptionMock)

        //Action
        val testObserver = gitRepoRepository
            .searchRepositories("mock")
            .test()

        //Test
        testObserver.assertNotComplete()
        testObserver.assertError(exceptionMock)
    }

}