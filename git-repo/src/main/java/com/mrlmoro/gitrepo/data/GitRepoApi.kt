package com.mrlmoro.gitrepo.data

import com.mrlmoro.gitrepo.data.dto.PagingDto
import com.mrlmoro.gitrepo.data.dto.RepositoryDto
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GitRepoApi {

    @GET("/repositories")
    fun getRepositories(
        @Query("since") since: Long? = null
    ): Single<List<RepositoryDto.Response>>

    @GET("/search/repositories")
    fun searchRepositories(
        @Query("q") query: String,
        @Query("page") page: Int? = null,
        @Query("per_page") pageSize: Int? = null
    ): Single<PagingDto<RepositoryDto.Response>>

}