package com.example.myweatherapp.domain.reducer

import com.example.myweatherapp.domain.model.ForecastResponse
import com.example.myweatherapp.domain.types.WeatherIntent
import com.example.myweatherapp.domain.types.WeatherState
import com.example.myweatherapp.testing.sampleWeather
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class WeatherReducerTest {
    @Test
    fun searchStartedSetsLoadingAndClearsError() {
        val state = WeatherState(errorMessage = "Boom")

        val next = weatherReducer(state, WeatherIntent.SearchStarted("London"))

        assertEquals("London", next.cityName)
        assertTrue(next.isLoading)
        assertNull(next.errorMessage)
    }

    @Test
    fun searchSucceededStoresWeatherAndForecast() {
        val weather = sampleWeather()
        val forecast = ForecastResponse(emptyList())

        val next = weatherReducer(
            WeatherState(isLoading = true),
            WeatherIntent.SearchSucceeded("London", weather, forecast)
        )

        assertEquals("London", next.cityName)
        assertSame(weather, next.currentWeather)
        assertSame(forecast, next.forecast)
        assertFalse(next.isLoading)
        assertNull(next.errorMessage)
    }

    @Test
    fun searchFailedStopsLoadingAndStoresMessage() {
        val next = weatherReducer(
            WeatherState(isLoading = true),
            WeatherIntent.SearchFailed("City not found")
        )

        assertFalse(next.isLoading)
        assertEquals("City not found", next.errorMessage)
    }
}
