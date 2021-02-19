package com.mrlmoro.gitrepo.data.repository

import com.mrlmoro.gitrepo.data.GitRepoApi
import com.mrlmoro.gitrepo.domain.model.Repository
import com.mrlmoro.gitrepo.domain.repository.GitRepoRepository
import io.reactivex.rxjava3.core.Single

class GitRepoRepositoryImp(private val api: GitRepoApi) : GitRepoRepository {

    override fun getRepositories(since: Long?): Single<List<Repository>> {
        return api.getRepositories(since)
            .flattenAsObservable { it }
            .map { it.toRepository() }
            .toList()
    }

    override fun searchRepositories(
        query: String,
        page: Int?,
        pageSize: Int?
    ): Single<List<Repository>> {
        return api.searchRepositories(query, page, pageSize)
            .flattenAsObservable { it.items }
            .map { it.toRepository() }
            .toList()
    }

}