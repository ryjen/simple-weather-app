package com.example.myweatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.myweatherapp.features.forecast.interactors.ForecastInteractor
import com.example.myweatherapp.features.forecast.types.ForecastEffect
import com.example.myweatherapp.features.forecast.types.ForecastIntent
import com.example.myweatherapp.features.forecast.views.ForecastScreen
import com.example.myweatherapp.features.weather.interactors.WeatherInteractor
import com.example.myweatherapp.features.weather.types.WeatherEffect
import com.example.myweatherapp.features.weather.views.CurrentWeatherScreen
import com.example.myweatherapp.features.welcome.interactors.WelcomeInteractor
import com.example.myweatherapp.features.welcome.types.WelcomeEffect
import com.example.myweatherapp.features.welcome.views.WelcomeScreen
import com.example.myweatherapp.ui.NavRoute
import com.example.myweatherapp.ui.theme.MyWeatherAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyWeatherAppTheme {
                WeatherApp()
            }
        }
    }
}

@Composable
fun WeatherApp() {
    val context = LocalContext.current
    val appGraph = remember { WeatherAppGraph(context.applicationContext) }
    val welcomeInteractor: WelcomeInteractor = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return appGraph.createWelcomeInteractor() as T
            }
        }
    )
    val forecastInteractor: ForecastInteractor = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return appGraph.createForecastInteractor() as T
            }
        }
    )
    val weatherInteractor: WeatherInteractor = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return appGraph.createWeatherInteractor() as T
            }
        }
    )
    val weatherState by weatherInteractor.state.collectAsState()
    val forecastState by forecastInteractor.state.collectAsState()
    val welcomeDispatch = welcomeInteractor::dispatch
    val forecastDispatch = forecastInteractor::dispatch
    val dispatch = weatherInteractor::dispatch

    val backStack = rememberNavBackStack(NavRoute.Welcome)

    LaunchedEffect(welcomeInteractor, backStack) {
        welcomeInteractor.effects.collect { effect ->
            when (effect) {
                WelcomeEffect.NavigateToWeather -> backStack.add(NavRoute.CurrentWeather)
            }
        }
    }

    LaunchedEffect(weatherInteractor, backStack) {
        weatherInteractor.effects.collect { effect ->
            when (effect) {
                WeatherEffect.NavigateToForecast -> backStack.add(NavRoute.Forecast)
            }
        }
    }

    LaunchedEffect(forecastInteractor, backStack) {
        forecastInteractor.effects.collect { effect ->
            when (effect) {
                ForecastEffect.NavigateBack -> if (backStack.size > 1) {
                    backStack.removeAt(backStack.size - 1)
                }

                ForecastEffect.NavigateToWeather -> {
                    // Navigate back to weather if it's in the stack, otherwise go to it.
                    // Since it's usually CurrentWeather -> Forecast, we just pop back.
                    if (backStack.size > 1) {
                        backStack.removeAt(backStack.size - 1)
                    } else {
                        backStack.add(NavRoute.CurrentWeather)
                    }
                }
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

    NavDisplay(
        backStack = backStack,
        onBack = { if (backStack.size > 1) backStack.removeAt(backStack.size - 1) },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = { key ->
            when (key) {
                is NavRoute.Welcome -> {
                    NavEntry(key) {
                        WelcomeScreen(
                            dispatch = welcomeDispatch
                        )
                    }
                }

                is NavRoute.CurrentWeather -> {
                    NavEntry(key) {
                        CurrentWeatherScreen(
                            state = weatherState,
                            dispatch = dispatch
                        )
                    }
                }

                is NavRoute.Forecast -> {
                    NavEntry(key) {
                        ForecastScreen(
                            state = forecastState,
                            dispatch = forecastDispatch
                        )
                    }
                }

                else -> NavEntry(key) { }
            }
        }
    )
}
