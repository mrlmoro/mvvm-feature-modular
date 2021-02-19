package com.mrlmoro.mvvmfeaturemodular

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
abstract class BaseTest {

    protected lateinit var mockWebServer: MockWebServer

    @Before
    fun setupTest() {
        mockWebServer = MockWebServer()
        mockWebServer.start(CustomTestRunner.MOCK_PORT)
    }

    @After
    fun finishTest() {
        mockWebServer.shutdown()
    }

    protected fun getContext() = InstrumentationRegistry
        .getInstrumentation()
        .targetContext
        .applicationContext

    protected fun readJson(fileName: String) = getContext()
        .assets
        .open(fileName)
        .readBytes()
        .toString(Charsets.UTF_8)
}