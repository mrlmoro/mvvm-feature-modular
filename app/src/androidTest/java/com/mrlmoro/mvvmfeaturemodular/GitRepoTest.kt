package com.mrlmoro.mvvmfeaturemodular

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Test
import java.lang.Thread.sleep

class GitRepoTest : BaseTest() {

    private val intent = Intent(
        ApplicationProvider.getApplicationContext(),
        MainActivity::class.java
    )

    @Test
    fun testRepoList_WhenSuccess() {
        mockRepositories(statusCode = 200)
        launchActivity<MainActivity>(intent).use {
            checkItemDisplayed()
        }
    }

    @Test
    fun testRepoList_WhenError_RetrySuccess() {
        mockRepositories(statusCode = 500)
        launchActivity<MainActivity>(intent).use {
            checkErrorDisplayed()
            mockRepositories(statusCode = 200)
            clickRetry()
            checkItemDisplayed()
        }
    }

    @Test
    fun testRepoList_WhenOpenDetail() {
        mockRepositories(statusCode = 200)
        launchActivity<MainActivity>(intent).use {
            clickItem()
            checkDetailsDisplayed()
        }
    }

    //Helpers

    private fun mockRepositories(statusCode: Int) {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(statusCode)
                    .setBody(readJson("json/repositories.json"))
            }
        }
    }

    private fun checkItemDisplayed() {
        onView(withText("mrlmoro/mvvm-feature-modular"))
            .check(matches(ViewMatchers.isDisplayed()))
    }

    private fun checkErrorDisplayed() {
        onView(withText(getContext().getString(R.string.unexpected_error)))
            .check(matches(ViewMatchers.isDisplayed()))

        sleep(500)
    }

    private fun clickRetry() {
        onView(withText(getContext().getString(R.string.retry)))
            .perform(ViewActions.click())

        sleep(1000)
    }

    private fun clickItem() {
        onView(withText("mrlmoro/mvvm-feature-modular"))
            .perform(ViewActions.click())

        sleep(1000)
    }

    private fun checkDetailsDisplayed() {
        onView(withText("Usuário: mrlmoro"))
            .check(matches(ViewMatchers.isDisplayed()))

        onView(withText("Repositório: mvvm-feature-modular"))
            .check(matches(ViewMatchers.isDisplayed()))

        onView(withText("mockDescription"))
            .check(matches(ViewMatchers.isDisplayed()))
    }
}