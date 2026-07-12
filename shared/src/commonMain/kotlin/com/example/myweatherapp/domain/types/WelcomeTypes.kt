package com.example.myweatherapp.domain.types

data object WelcomeState

sealed interface WelcomeIntent {
    data object GetStartedClicked : WelcomeIntent
}

sealed interface WelcomeEffect {
    data object NavigateToWeather : WelcomeEffect
}
