package com.example.myweatherapp.ios

import com.example.myweatherapp.domain.interactor.WeatherInteractor
import com.example.myweatherapp.domain.types.WeatherIntent
import com.example.myweatherapp.domain.types.WeatherState
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * A wrapper for WeatherInteractor to be used directly from SwiftUI.
 */
class IosWeatherInteractor : KoinComponent {
    private val interactor: WeatherInteractor by inject()

    val state: FlowWrapper<WeatherState> = FlowWrapper(interactor.state)

    fun dispatch(intent: WeatherIntent) {
        interactor.dispatch(intent)
    }
}
