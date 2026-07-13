package com.example.myweatherapp.features.forecast.types

import com.example.myweatherapp.data.model.ForecastResponse
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ForecastState(
    val days: List<DailyForecast> = emptyList(),
    val isLoading: Boolean = false
)

data class DailyForecast(
    val dateLabel: String,
    val hours: List<HourlyForecast>
)

data class HourlyForecast(
    val timeLabel: String,
    val tempCelsius: Int,
    val feelsLikeCelsius: Int,
    val description: String,
    val icon: String?
)

sealed interface ForecastIntent {
    data class ForecastDataChanged(
        val forecast: ForecastResponse?,
        val isLoading: Boolean
    ) : ForecastIntent

    data object BackClicked : ForecastIntent
}

sealed interface ForecastEffect {
    data object NavigateBack : ForecastEffect
}

fun ForecastResponse.toDailyForecasts(): List<DailyForecast> {
    val dayFormatter = SimpleDateFormat("EEE, MMM d", Locale.getDefault())
    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())

    return list
        .sortedBy { it.dt }
        .groupBy { item -> dayFormatter.format(Date(item.dt * 1000L)) }
        .map { (day, items) ->
            DailyForecast(
                dateLabel = day,
                hours = items.map { item ->
                    HourlyForecast(
                        timeLabel = timeFormatter.format(Date(item.dt * 1000L)),
                        tempCelsius = item.main.temp.toInt(),
                        feelsLikeCelsius = item.main.feelsLike.toInt(),
                        description = item.weather.firstOrNull()?.description?.replaceFirstChar { it.uppercase() }.orEmpty(),
                        icon = item.weather.firstOrNull()?.icon
                    )
                }
            )
        }
}
