package com.example.myweatherapp.e2e

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.myweatherapp.MainActivity
import org.junit.Rule
import org.junit.Test

class WeatherAppE2ETest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

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
}
