package com.mrlmoro.gitrepo.domain.repository

import com.mrlmoro.gitrepo.domain.model.Repository
import io.reactivex.rxjava3.core.Single

interface GitRepoRepository {

    fun getRepositories(since: Long? = null): Single<List<Repository>>

    fun searchRepositories(
        query: String,
        page: Int? = null,
        pageSize: Int? = null
    ): Single<List<Repository>>

}