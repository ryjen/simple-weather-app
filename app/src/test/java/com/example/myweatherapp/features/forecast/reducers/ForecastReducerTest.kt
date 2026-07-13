package com.example.myweatherapp.features.forecast.reducers

import com.example.myweatherapp.data.model.ForecastItem
import com.example.myweatherapp.data.model.ForecastResponse
import com.example.myweatherapp.data.model.MainWeather
import com.example.myweatherapp.data.model.WeatherDescription
import com.example.myweatherapp.features.forecast.types.DailyForecast
import com.example.myweatherapp.features.forecast.types.ForecastIntent
import com.example.myweatherapp.features.forecast.types.ForecastState
import com.example.myweatherapp.features.forecast.types.HourlyForecast
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ForecastReducerTest {
    @Test
    fun forecastDataChangedGroupsForecastItemsByDayAndKeepsHourlyEntries() {
        val forecast = ForecastResponse(
            list = listOf(
                forecastItem(dt = 1625068800, temp = 20.0, description = "few clouds", icon = "02d"),
                forecastItem(dt = 1625079600, temp = 18.0, description = "light rain", icon = "10d"),
                forecastItem(dt = 1625155200, temp = 22.0, description = "clear sky", icon = "01d")
            )
        )

        val next = forecastReducer(
            ForecastState(),
            ForecastIntent.ForecastDataChanged(forecast = forecast, isLoading = true)
        )

        assertEquals(2, next.days.size)
        assertEquals(2, next.days.first().hours.size)
        assertEquals(listOf(20, 18), next.days.first().hours.map { it.tempCelsius })
        assertEquals(listOf(21, 19), next.days.first().hours.map { it.feelsLikeCelsius })
        assertEquals("Few clouds", next.days.first().hours.first().description)
        assertEquals("02d", next.days.first().hours.first().icon)
        assertTrue(next.isLoading)
    }

    @Test
    fun forecastDataChangedWithNullForecastClearsDays() {
        val next = forecastReducer(
            ForecastState(days = listOf(sampleDay())),
            ForecastIntent.ForecastDataChanged(forecast = null, isLoading = false)
        )

        assertTrue(next.days.isEmpty())
        assertFalse(next.isLoading)
    }

    @Test
    fun backClickedDoesNotMutateState() {
        val state = ForecastState(days = listOf(sampleDay()), isLoading = false)

        val next = forecastReducer(state, ForecastIntent.BackClicked)

        assertEquals(state, next)
        assertFalse(next.isLoading)
    }

    private fun forecastItem(
        dt: Long,
        temp: Double,
        description: String,
        icon: String
    ): ForecastItem {
        return ForecastItem(
            dt = dt,
            main = MainWeather(temp, temp + 1, 60, 1012),
            weather = listOf(WeatherDescription("Clouds", description, icon)),
            dtTxt = ""
        )
    }

    private fun sampleDay() = DailyForecast(
        dateLabel = "Wed, Jun 30",
        hours = listOf(
            HourlyForecast(
                timeLabel = "12:00",
                tempCelsius = 20,
                feelsLikeCelsius = 22,
                description = "Few clouds",
                icon = "02d"
            )
        )
    )
}
