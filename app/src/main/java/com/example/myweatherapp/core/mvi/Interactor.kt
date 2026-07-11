package com.example.myweatherapp.core.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

typealias Reducer<State, Intent> = (State, Intent) -> State

abstract class Interactor<State, Intent, Effect>(
    initialState: State,
    private val reducer: Reducer<State, Intent>
) : ViewModel() {
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<State> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<Effect>()
    val effects: SharedFlow<Effect> = _effects.asSharedFlow()

    fun dispatch(intent: Intent) {
        _state.value = reducer(_state.value, intent)
        induce(intent, _state.value)
    }

    protected open fun induce(intent: Intent, state: State) = Unit

    protected fun emit(effect: Effect) {
        viewModelScope.launch { _effects.emit(effect) }
    }
}
