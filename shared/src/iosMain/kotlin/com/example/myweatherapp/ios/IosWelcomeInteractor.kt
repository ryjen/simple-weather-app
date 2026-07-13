package com.example.myweatherapp.ios

import com.example.myweatherapp.domain.interactor.WelcomeInteractor
import com.example.myweatherapp.domain.types.WelcomeEffect
import com.example.myweatherapp.domain.types.WelcomeIntent
import com.example.myweatherapp.domain.types.WelcomeState
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class IosWelcomeInteractor : KoinComponent {
    private val interactor: WelcomeInteractor by inject()

    val state: FlowWrapper<WelcomeState> = FlowWrapper(interactor.state)
    val effects: FlowWrapper<WelcomeEffect> = FlowWrapper(interactor.effects)

    fun dispatch(intent: WelcomeIntent) {
        interactor.dispatch(intent)
    }
}
