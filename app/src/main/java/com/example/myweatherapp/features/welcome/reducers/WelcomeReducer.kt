package com.example.myweatherapp.features.welcome.reducers

import com.example.myweatherapp.features.welcome.types.WelcomeIntent
import com.example.myweatherapp.features.welcome.types.WelcomeState

fun welcomeReducer(state: WelcomeState, intent: WelcomeIntent): WelcomeState {
    return when (intent) {
        WelcomeIntent.GetStartedClicked -> state
    }
}
