package com.example.myweatherapp.data.local

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

private val Context.dataStore by preferencesDataStore(name = "test_weather_prefs")

@RunWith(AndroidJUnit4::class)
class WeatherPreferencesIntegrationTest {
    @Test
    fun saveLastCityPersistsValueInDataStore() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val preferences = WeatherPreferences(context.dataStore)
        val city = "Integration City ${System.nanoTime()}"

        preferences.saveLastCity(city)

        assertEquals(city, preferences.lastCityFlow.first())
    }
}
