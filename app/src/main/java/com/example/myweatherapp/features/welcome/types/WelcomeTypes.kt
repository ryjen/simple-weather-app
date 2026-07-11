package com.example.myweatherapp.features.welcome.types

data object WelcomeState

sealed interface WelcomeIntent {
    data object GetStartedClicked : WelcomeIntent
}

sealed interface WelcomeEffect {
    data object NavigateToWeather : WelcomeEffect
}
