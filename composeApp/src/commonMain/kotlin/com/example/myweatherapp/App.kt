package com.example.myweatherapp

import androidx.compose.runtime.*
import com.example.myweatherapp.domain.interactor.WeatherInteractor
import com.example.myweatherapp.domain.interactor.WelcomeInteractor
import com.example.myweatherapp.domain.types.WelcomeEffect
import com.example.myweatherapp.ui.screens.CurrentWeatherScreen
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
        
        val weatherState by weatherInteractor.state.collectAsState()
        
        LaunchedEffect(welcomeInteractor) {
            welcomeInteractor.effects.collect { effect ->
                when (effect) {
                    WelcomeEffect.NavigateToWeather -> currentScreen = Screen.Weather
                }
            }
        }
        
        when (currentScreen) {
            Screen.Welcome -> WelcomeScreen(dispatch = { welcomeInteractor.dispatch(it) })
            Screen.Weather -> CurrentWeatherScreen(
                state = weatherState,
                dispatch = { weatherInteractor.dispatch(it) }
            )
        }
    }
}

sealed class Screen {
    data object Welcome : Screen()
    data object Weather : Screen()
}
