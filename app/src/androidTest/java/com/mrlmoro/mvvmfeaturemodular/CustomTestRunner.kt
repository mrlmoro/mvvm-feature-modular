package com.mrlmoro.mvvmfeaturemodular

import android.app.Application
import androidx.test.runner.AndroidJUnitRunner
import com.mrlmoro.core.data.RetrofitConfig
import com.mrlmoro.gitrepo.data.GitRepoApi
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

class CustomTestRunner : AndroidJUnitRunner() {

    override fun callApplicationOnCreate(app: Application?) {
        super.callApplicationOnCreate(app)
        loadKoinModules(module {
            single<GitRepoApi>(override = true) { RetrofitConfig.create(MOCK_URL) }
        })
    }

    companion object {
        const val MOCK_PORT = 8080
        const val MOCK_URL = "http://localhost:$MOCK_PORT"
    }

}