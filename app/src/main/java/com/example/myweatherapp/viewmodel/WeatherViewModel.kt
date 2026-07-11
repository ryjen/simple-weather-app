package com.example.myweatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherapp.data.model.CurrentWeatherResponse
import com.example.myweatherapp.data.model.ForecastResponse
import com.example.myweatherapp.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

sealed class WeatherUiState {
    object Idle : WeatherUiState()
    object Loading : WeatherUiState()
    data class Success(
        val currentWeather: CurrentWeatherResponse,
        val forecast: ForecastResponse
    ) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Idle)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    init {
        loadLastSearchedCity()
    }

    private fun loadLastSearchedCity() {
        viewModelScope.launch {
            val lastCity = repository.getLastCity().firstOrNull()
            if (!lastCity.isNullOrBlank()) {
                searchCity(lastCity)
            }
        }
    }

    fun searchCity(cityName: String) {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            try {
                val coords = repository.getCoordinates(cityName)
                if (coords.isNotEmpty()) {
                    val location = coords[0]
                    val currentWeather = repository.getCurrentWeather(location.lat, location.lon)
                    val forecast = repository.getForecast(location.lat, location.lon)
                    
                    repository.saveLastCity(cityName)
                    _uiState.value = WeatherUiState.Success(currentWeather, forecast)
                } else {
                    _uiState.value = WeatherUiState.Error("City not found")
                }
            } catch (e: Exception) {
                _uiState.value = WeatherUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
    
    fun retry(cityName: String) {
        searchCity(cityName)
    }
}
