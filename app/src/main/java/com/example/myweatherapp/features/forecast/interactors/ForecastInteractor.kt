package com.example.myweatherapp.features.forecast.interactors

import androidx.lifecycle.viewModelScope
import com.example.myweatherapp.core.mvi.Interactor
import com.example.myweatherapp.features.forecast.reducers.forecastReducer
import com.example.myweatherapp.features.forecast.types.ForecastEffect
import com.example.myweatherapp.features.forecast.types.ForecastIntent
import com.example.myweatherapp.features.forecast.types.ForecastState
import com.example.myweatherapp.features.weather.usecases.SearchWeatherUseCase
import kotlinx.coroutines.launch

class ForecastInteractor(
    private val searchWeatherUseCase: SearchWeatherUseCase
) : Interactor<ForecastState, ForecastIntent, ForecastEffect>(
    ForecastState(),
    ::forecastReducer
) {
    override fun induce(intent: ForecastIntent, state: ForecastState) {
        when (intent) {
            ForecastIntent.BackClicked -> emit(ForecastEffect.NavigateBack)
            ForecastIntent.SearchSubmitted -> search(state.cityName)
            is ForecastIntent.CitySelected -> onCitySelected(intent.location)
            is ForecastIntent.ForecastDataChanged,
            is ForecastIntent.CityNameChanged,
            is ForecastIntent.SearchFailed,
            is ForecastIntent.SearchResultsFound,
            ForecastIntent.SearchStarted -> Unit
        }
    }

    private fun search(cityName: String) {
        if (cityName.isBlank()) return
        dispatch(ForecastIntent.SearchStarted)
        viewModelScope.launch {
            runCatching { searchWeatherUseCase.searchCities(cityName) }
                .onSuccess { results ->
                    if (results.isEmpty()) {
                        dispatch(ForecastIntent.SearchFailed("City not found"))
                    } else if (results.size == 1) {
                        onCitySelected(results.first())
                    } else {
                        dispatch(ForecastIntent.SearchResultsFound(results))
                    }
                }
                .onFailure { error ->
                    dispatch(ForecastIntent.SearchFailed(error.message ?: "Unknown error"))
                }
        }
    }

    private fun onCitySelected(location: com.example.myweatherapp.domain.model.GeocodingResponse) {
        viewModelScope.launch {
            searchWeatherUseCase.fetchWeatherForLocation(location)
            emit(ForecastEffect.NavigateToWeather)
        }
    }
}
