package com.dicoding.storyapp.views.login

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityTest {
    @get:Rule
    val activity = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun testLoginLogout_Success() {
        onView(withId(R.id.emailEditText))
            .perform(
            ViewActions.typeText("test@gmail.com"),
            ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.passwordEditText))
            .perform(
            ViewActions.typeText("12345678"),
            ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.btnLogin))
            .perform(click())
        Thread.sleep(5000)
        onView(withText(R.string.success))
            .check(matches(isDisplayed()))
        onView(withText(R.string.next))
            .perform(click())
        onView(withId(R.id.fabLogout))
            .check(matches(isDisplayed()))
        onView(withId(R.id.fabLogout))
            .perform(click())
        onView(withId(R.id.btnLogin))
            .check(matches(isDisplayed()))
    }
}