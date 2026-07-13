package com.example.myweatherapp.domain.interactor

import com.example.myweatherapp.core.mvi.Interactor
import com.example.myweatherapp.domain.reducer.forecastReducer
import com.example.myweatherapp.domain.types.ForecastEffect
import com.example.myweatherapp.domain.types.ForecastIntent
import com.example.myweatherapp.domain.types.ForecastState

class ForecastInteractor : Interactor<ForecastState, ForecastIntent, ForecastEffect>(
    ForecastState(),
    ::forecastReducer
) {
    override fun induce(intent: ForecastIntent, state: ForecastState) {
        when (intent) {
            ForecastIntent.BackClicked -> emit(ForecastEffect.NavigateBack)
            is ForecastIntent.ForecastDataChanged -> Unit
        }
    }
}
