package com.example.myweatherapp.ui

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface NavRoute : NavKey {
    @Serializable
    data object Welcome : NavRoute

    @Serializable
    data object CurrentWeather : NavRoute

    @Serializable
    data object Forecast : NavRoute
}
