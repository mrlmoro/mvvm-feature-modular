package com.mrlmoro.mvvmfeaturemodular

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import org.hamcrest.core.AllOf.allOf
import org.junit.Test
import java.lang.Thread.sleep

class GitRepoSearchTest : BaseTest() {

    private val intent = Intent(
        ApplicationProvider.getApplicationContext(),
        MainActivity::class.java
    )

    @Test
    fun testRepoSearch_whenSuccess() {
        mockRepositories(statusCode = 200)
        launchActivity<MainActivity>(intent).use {
            clickBottomNavSearchItem()
            performSearch()
            checkItemDisplayed()
        }
    }

    @Test
    fun testRepoSearch_whenNotFound() {
        mockRepositories(statusCode = 200, fileName = "json/paging-empty.json")
        launchActivity<MainActivity>(intent).use {
            clickBottomNavSearchItem()
            performSearch()
            checkNotFoundDisplayed()
        }
    }

    @Test
    fun testRepoSearch_whenError_RetrySuccess() {
        mockRepositories(statusCode = 500)
        launchActivity<MainActivity>(intent).use {
            clickBottomNavSearchItem()
            performSearch()
            checkErrorDisplayed()
            mockRepositories(statusCode = 200)
            clickRetry()
            checkItemDisplayed()
        }
    }

    //Helpers

    private fun mockRepositories(
        statusCode: Int,
        fileName: String = "json/paging-repositories.json"
    ) {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(statusCode)
                    .setBody(readJson(fileName))
            }
        }
    }

    private fun clickBottomNavSearchItem() {
        onView(
            allOf(
                withText(getContext().getString(R.string.search)),
                isDescendantOfA(withId(R.id.bottom_nav)),
                isDisplayed()
            )
        ).perform(ViewActions.click())

        sleep(1000)
    }

    private fun performSearch() {
        onView(withId(R.id.et_search)).perform(
            ViewActions.replaceText("mock"),
            ViewActions.closeSoftKeyboard()
        )

        onView(withId(R.id.et_search))
            .perform(ViewActions.pressImeActionButton())

        sleep(1000)
    }

    private fun checkItemDisplayed() {
        onView(withText("mrlmoro/mvvm-feature-modular"))
            .check(matches(isDisplayed()))
    }

    private fun checkErrorDisplayed() {
        onView(withText(getContext().getString(R.string.unexpected_error)))
            .check(matches(isDisplayed()))

        sleep(500)
    }

    private fun clickRetry() {
        onView(withText(getContext().getString(R.string.retry)))
            .perform(ViewActions.click())

        sleep(1000)
    }

    private fun checkNotFoundDisplayed() {
        onView(withText(getContext().getString(R.string.repository_not_found)))
            .check(matches(isDisplayed()))
    }
}