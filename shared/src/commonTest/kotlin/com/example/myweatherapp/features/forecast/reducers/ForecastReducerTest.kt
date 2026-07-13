package com.example.myweatherapp.domain.reducer

import com.example.myweatherapp.domain.types.ForecastIntent
import com.example.myweatherapp.domain.types.ForecastState
import com.example.myweatherapp.testing.sampleForecast
import org.junit.Assert.assertFalse
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class ForecastReducerTest {
    @Test
    fun forecastDataChangedStoresForecastAndLoadingState() {
        val forecast = sampleForecast()

        val next = forecastReducer(
            ForecastState(),
            ForecastIntent.ForecastDataChanged(forecast = forecast, isLoading = true)
        )

        assertSame(forecast, next.forecast)
        assertTrue(next.isLoading)
    }

    @Test
    fun backClickedDoesNotMutateState() {
        val state = ForecastState(forecast = sampleForecast(), isLoading = false)

        val next = forecastReducer(state, ForecastIntent.BackClicked)

        assertSame(state, next)
        assertFalse(next.isLoading)
    }
}
