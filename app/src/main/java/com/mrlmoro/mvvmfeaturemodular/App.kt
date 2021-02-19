package com.mrlmoro.mvvmfeaturemodular

import android.app.Application
import com.mrlmoro.gitrepo.di.gitRepoModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            androidFileProperties()
            modules(listOf(gitRepoModule))
        }
    }

}