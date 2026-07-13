package com.example.myweatherapp.e2e

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import com.example.myweatherapp.MainActivity
import com.example.myweatherapp.data.local.SharedPreferencesOnboardingRepository
import com.example.myweatherapp.data.local.WeatherPreferences
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WeatherAppE2ETest {
    @get:Rule
    val composeRule = createEmptyComposeRule()

    private var scenario: ActivityScenario<MainActivity>? = null

    @Before
    fun setUp() {
        clearAppState()
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun tearDown() {
        scenario?.close()
        scenario = null
        clearAppState()
    }

    @Test
    fun launchShowsWelcomeScreen() {
        composeRule.onNodeWithText("Welcome to MyWeatherApp").assertIsDisplayed()
        composeRule.onNodeWithTag("welcome_get_started").assertIsDisplayed()
    }

    @Test
    fun forecastDisabledBeforeSearch() {
        composeRule.onNodeWithTag("welcome_get_started").performClick()

        composeRule.onNodeWithText("Current Weather").assertIsDisplayed()
        composeRule.onNodeWithTag("current_weather_search").assertIsDisplayed()
        composeRule.onNodeWithTag("current_weather_forecast").assertIsNotEnabled()
    }

    private fun clearAppState() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        SharedPreferencesOnboardingRepository(context).clear()
        runBlocking {
            WeatherPreferences(context).clear()
        }
    }
}
