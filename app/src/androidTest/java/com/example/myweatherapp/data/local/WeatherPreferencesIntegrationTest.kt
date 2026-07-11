package com.example.myweatherapp.data.local

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeatherPreferencesIntegrationTest {
    @Test
    fun saveLastCityPersistsValueInDataStore() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val preferences = WeatherPreferences(context)
        val city = "Integration City ${System.nanoTime()}"

        preferences.saveLastCity(city)

        assertEquals(city, preferences.lastCityFlow.first())
    }
}
