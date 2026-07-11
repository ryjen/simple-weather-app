package com.example.myweatherapp.features.forecast.interactors

import com.example.myweatherapp.core.mvi.Interactor
import com.example.myweatherapp.features.forecast.reducers.forecastReducer
import com.example.myweatherapp.features.forecast.types.ForecastEffect
import com.example.myweatherapp.features.forecast.types.ForecastIntent
import com.example.myweatherapp.features.forecast.types.ForecastState

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
