package com.example.myweatherapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.myweatherapp.domain.interactor.ForecastInteractor
import com.example.myweatherapp.domain.interactor.WeatherInteractor
import com.example.myweatherapp.domain.interactor.WelcomeInteractor
import com.example.myweatherapp.domain.types.ForecastEffect
import com.example.myweatherapp.domain.types.ForecastIntent
import com.example.myweatherapp.domain.types.WeatherEffect
import com.example.myweatherapp.domain.types.WelcomeEffect
import com.example.myweatherapp.ui.screens.CurrentWeatherScreen
import com.example.myweatherapp.ui.screens.ForecastScreen
import com.example.myweatherapp.ui.screens.WelcomeScreen
import com.example.myweatherapp.ui.theme.MyWeatherAppTheme
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun App() {
    MyWeatherAppTheme {
        var currentScreen by remember { mutableStateOf<Screen>(Screen.Welcome) }

        val welcomeInteractor: WelcomeInteractor = koinViewModel()
        val weatherInteractor: WeatherInteractor = koinViewModel()
        val forecastInteractor: ForecastInteractor = koinViewModel()

        val weatherState by weatherInteractor.state.collectAsState()
        val forecastState by forecastInteractor.state.collectAsState()

        LaunchedEffect(welcomeInteractor) {
            welcomeInteractor.effects.collect { effect ->
                when (effect) {
                    WelcomeEffect.NavigateToWeather -> currentScreen = Screen.Weather
                }
            }
        }

        LaunchedEffect(weatherInteractor) {
            weatherInteractor.effects.collect { effect ->
                when (effect) {
                    WeatherEffect.NavigateToForecast -> currentScreen = Screen.Forecast
                }
            }
        }

        LaunchedEffect(forecastInteractor) {
            forecastInteractor.effects.collect { effect ->
                when (effect) {
                    ForecastEffect.NavigateBack -> currentScreen = Screen.Weather
                }
            }
        }

        LaunchedEffect(weatherState.forecast, weatherState.isLoading) {
            forecastInteractor.dispatch(
                ForecastIntent.ForecastDataChanged(
                    forecast = weatherState.forecast,
                    isLoading = weatherState.isLoading
                )
            )
        }

        when (currentScreen) {
            Screen.Welcome -> WelcomeScreen(dispatch = { welcomeInteractor.dispatch(it) })
            Screen.Weather -> CurrentWeatherScreen(
                state = weatherState,
                dispatch = { weatherInteractor.dispatch(it) }
            )
            Screen.Forecast -> ForecastScreen(
                state = forecastState,
                dispatch = { forecastInteractor.dispatch(it) }
            )
        }
    }
}

sealed class Screen {
    data object Welcome : Screen()
    data object Weather : Screen()
    data object Forecast : Screen()
}
