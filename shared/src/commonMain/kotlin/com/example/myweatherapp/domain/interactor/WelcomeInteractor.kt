package com.example.myweatherapp.domain.interactor

import com.example.myweatherapp.core.mvi.Interactor
import com.example.myweatherapp.domain.reducer.welcomeReducer
import com.example.myweatherapp.domain.types.WelcomeEffect
import com.example.myweatherapp.domain.types.WelcomeIntent
import com.example.myweatherapp.domain.types.WelcomeState

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
