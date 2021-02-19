package com.mrlmoro.gitrepo.di

import com.mrlmoro.core.Properties
import com.mrlmoro.core.data.RetrofitConfig
import com.mrlmoro.gitrepo.data.GitRepoApi
import com.mrlmoro.gitrepo.data.repository.GitRepoRepositoryImp
import com.mrlmoro.gitrepo.domain.repository.GitRepoRepository
import com.mrlmoro.gitrepo.ui.list.GitRepoListViewModel
import com.mrlmoro.gitrepo.ui.search.GitRepoSearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val gitRepoModule = module {

    single<GitRepoApi> {
        RetrofitConfig.create(getProperty(Properties.GITHUB_URL))
    }

    factory<GitRepoRepository> { GitRepoRepositoryImp(get()) }

    viewModel { GitRepoListViewModel(get()) }
    viewModel { GitRepoSearchViewModel(get()) }
}