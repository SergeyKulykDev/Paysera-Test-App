package com.serhiikulyk.payseratestapp.synchronizer

import android.util.Log
import com.serhiikulyk.payseratestapp.data.prefs.Prefs
import com.serhiikulyk.payseratestapp.data.repository.CurrencyRatesRepository
import com.serhiikulyk.payseratestapp.di.ExternalScope
import com.serhiikulyk.payseratestapp.utils.result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import javax.inject.Inject

class Synchronizer @Inject constructor(
    @ExternalScope private val externalScope: CoroutineScope,
    private val currencyRatesRepository: CurrencyRatesRepository,
    private val prefs: Prefs,
    private val eventBus: CustomEventBus
) {

    private var job: Job? = null
    private var lastQueryFailed = false
    private var timerInterval = 5000L

    fun startSynchronization() {
        job = externalScope.launchPeriodicAsync(timerInterval) { synchronizeNow() }
    }

    private suspend fun synchronizeNow() {
        Log.v(TAG, "--------------------------------------------")
        Log.v(TAG, "start synchronization")
        eventBus.emitEvent(AppEvent.START_SYNCING)

        val networkCurrencyRatesResult = result {
            currencyRatesRepository.fetchAndSaveCurrencyRates()
        }

        if (networkCurrencyRatesResult.isSuccess) {
            val networkCurrencyRates = networkCurrencyRatesResult.getOrNull()
            if (networkCurrencyRates != null) {
                successSync()
            } else {
                failedSync(NullPointerException("Current currency rates can't be null!"))
            }
        } else {
            failedSync(networkCurrencyRatesResult.exceptionOrNull())
        }
    }

    private suspend fun successSync() {
        eventBus.emitEvent(AppEvent.SUCCESS_SYNCING)
        Log.v(TAG, "success synchronization")
        restartTimer()
    }

    private suspend fun failedSync(e: Throwable?) {
        eventBus.emitEvent(AppEvent.FAILED_SYNCING)

        e?.printStackTrace()
        lastQueryFailed = true

        Log.e(TAG, "failed synchronization")
        restartTimer()
    }

    private suspend fun restartTimer() {
        job?.cancel()


        Log.d(TAG, "next synchronization in ${timerInterval / 1000} s")

        job = externalScope.launchPeriodicAsync(timerInterval) { synchronizeNow() }
    }

    private fun CoroutineScope.launchPeriodicAsync(
        repeatMillis: Long,
        action: suspend () -> Unit
    ) = this.async {
        if (repeatMillis > 0) {
            while (isActive) {
                delay(repeatMillis)
                action()
            }
        } else {
            async { action() }
        }
    }

    fun stopSynchronization() {
        job?.cancel()
        Log.d(TAG, "Stopped")
    }

    companion object {
        private const val TAG = "Synchronizer"
    }
}