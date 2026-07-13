package com.example.myweatherapp.ios

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * A wrapper for Kotlin Flow to be easily consumable from Swift.
 */
class FlowWrapper<T : Any>(private val flow: Flow<T>) {
    fun subscribe(
        onEach: (T) -> Unit,
        onComplete: () -> Unit,
        onThrow: (Throwable) -> Unit
    ): Job {
        return flow.onEach { onEach(it) }
            .launchIn(CoroutineScope(Dispatchers.Main))
    }
}
