package com.example.myweatherapp.domain.reducer

import com.example.myweatherapp.domain.types.WelcomeIntent
import com.example.myweatherapp.domain.types.WelcomeState

fun welcomeReducer(state: WelcomeState, intent: WelcomeIntent): WelcomeState {
    return when (intent) {
        WelcomeIntent.GetStartedClicked -> state
    }
}
