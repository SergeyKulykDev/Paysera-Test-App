package com.serhiikulyk.payseratestapp.synchronizer

import android.util.Log
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class CustomEventBus {

    private val _events = MutableSharedFlow<AppEvent>()
    val events = _events.asSharedFlow()

    suspend fun emitEvent(event: AppEvent) {
        Log.d(TAG, "Emitting event = $event")
        _events.emit(event)
    }

    fun tryEmitEvent(event: AppEvent) {
        Log.d(TAG, "Emitting event = $event")
        _events.tryEmit(event)
    }

    companion object {
        private const val TAG = "CustomEventBus"
    }
}

enum class AppEvent {
    START_SYNCING,
    SUCCESS_SYNCING,
    FAILED_SYNCING;
}