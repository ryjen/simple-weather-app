package com.example.myweatherapp.features.welcome.interactors

import com.example.myweatherapp.core.mvi.Interactor
import com.example.myweatherapp.features.welcome.reducers.welcomeReducer
import com.example.myweatherapp.features.welcome.types.WelcomeEffect
import com.example.myweatherapp.features.welcome.types.WelcomeIntent
import com.example.myweatherapp.features.welcome.types.WelcomeState

class WelcomeInteractor : Interactor<WelcomeState, WelcomeIntent, WelcomeEffect>(
    WelcomeState,
    ::welcomeReducer
) {
    override fun induce(intent: WelcomeIntent, state: WelcomeState) {
        when (intent) {
            WelcomeIntent.GetStartedClicked -> emit(WelcomeEffect.NavigateToWeather)
        }
    }
}
