package com.serhiikulyk.payseratestapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serhiikulyk.payseratestapp.const.CURRENCY_EUR
import com.serhiikulyk.payseratestapp.const.CURRENCY_USD
import com.serhiikulyk.payseratestapp.data.local.model.toBalanceItem
import com.serhiikulyk.payseratestapp.data.repository.BalancesRepository
import com.serhiikulyk.payseratestapp.synchronizer.AppEvent
import com.serhiikulyk.payseratestapp.synchronizer.CustomEventBus
import com.serhiikulyk.payseratestapp.synchronizer.Synchronizer
import com.serhiikulyk.payseratestapp.use_cases.ConversionResult
import com.serhiikulyk.payseratestapp.use_cases.ConvertCurrencyUseCase
import com.serhiikulyk.payseratestapp.use_cases.HasMoneyOnBalanceUseCase
import com.serhiikulyk.payseratestapp.use_cases.PreviewConvertCurrencyUseCase
import com.serhiikulyk.payseratestapp.use_cases.SaveDefaultBalancesUseCase
import com.serhiikulyk.payseratestapp.utils.result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    saveDefaultBalancesUseCase: SaveDefaultBalancesUseCase,
    private val balancesRepository: BalancesRepository,
    private val synchronizer: Synchronizer,
    private val eventBus: CustomEventBus,
    private val hasMoneyOnBalanceUseCase: HasMoneyOnBalanceUseCase,
    private val сonvertCurrencyUseCase: ConvertCurrencyUseCase,
    private val previewConvertCurrencyUseCase: PreviewConvertCurrencyUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    init {
        synchronizer.startSynchronization()

        saveDefaultBalancesUseCase()
        collectBalances()
        collectAppEvents()
    }

    private fun collectAppEvents() {
        viewModelScope.launch {
            eventBus.events.collect { appEvent ->
                _uiState.update {
                    when (appEvent) {
                        AppEvent.START_SYNCING -> it.copy(isSyncing = true)
                        AppEvent.SUCCESS_SYNCING -> it.copy(syncError = false, isSyncing = false)
                        AppEvent.FAILED_SYNCING -> it.copy(syncError = true, isSyncing = false)
                    }
                }
            }
        }
    }

    private fun collectBalances() {
        viewModelScope.launch {
            balancesRepository.getBalancesStream().collect { list ->
                _uiState.update {
                    it.copy(balances = list.map { entity ->
                        entity.toBalanceItem()
                    })
                }
            }
        }
    }

    fun onSellChange(text: String?) {
        val amount = try {
            text?.toDouble()!!
        } catch (e: NumberFormatException) {
            0.0
        }
        _uiState.update { it.copy(sell = amount) }
        checkHasMoneyOnBalance(amount)
        calculateReceivedMoney(amount)
    }

    private fun checkHasMoneyOnBalance(amount: Double) {
        viewModelScope.launch {
            val hasMoney = hasMoneyOnBalanceUseCase(amount, _uiState.value.sellCurrency)
            _uiState.update {
                it.copy(
                    isSubmitAvailable = hasMoney,
                    error = if (!hasMoney) "Insufficient balance" else null
                )
            }
        }
    }

    private fun calculateReceivedMoney(amount: Double) {
        viewModelScope.launch {
            val value = _uiState.value
            val result = result {
                previewConvertCurrencyUseCase(
                    amount = amount,
                    sellCurrency = value.sellCurrency,
                    buyCurrency = value.receiveCurrency
                )
            }
            _uiState.update {
                if (result.isSuccess) {
                    it.copy(receive = result.getOrNull() ?: 0.0)
                } else {
                    it.copy(receive = 0.0, error = result.exceptionOrNull()?.message)
                }
            }
        }
    }

    fun onSellCurrencyChanged(currency: String) {
        _uiState.update { it.copy(sellCurrency = currency) }

        val amount = _uiState.value.sell
        checkHasMoneyOnBalance(amount)
        calculateReceivedMoney(amount)
    }

    fun onReceiveCurrencyChanged(currency: String) {
        _uiState.update { it.copy(receiveCurrency = currency) }

        val amount = _uiState.value.sell
        checkHasMoneyOnBalance(amount)
        calculateReceivedMoney(amount)
    }

    fun submit() {
        viewModelScope.launch {
            val value = _uiState.value
            val result = result {
                сonvertCurrencyUseCase(
                    amount = value.sell,
                    sellCurrency = value.sellCurrency,
                    buyCurrency = value.receiveCurrency
                )
            }
            _uiState.update {
                if (result.isSuccess) {
                    it.copy(
                        conversionResult = result.getOrNull(),
                        sell = 0.0,
                        receive = 0.0
                    )
                } else {
                    it.copy(error = result.exceptionOrNull()?.message)
                }
            }
        }
    }

    fun onConvertedShown() {
        _uiState.update { it.copy(conversionResult = null) }
    }

    fun onErrorShown() {
        _uiState.update { it.copy(error = null) }
    }

    override fun onCleared() {
        super.onCleared()
        synchronizer.stopSynchronization()
    }
}

data class MainUiState(
    val balances: List<BalanceItem> = emptyList(),
    val sell: Double = 0.0,
    val receive: Double = 0.0,
    val sellCurrency: String = CURRENCY_EUR,
    val receiveCurrency: String = CURRENCY_USD,
    val isSyncing: Boolean = false,
    val syncError: Boolean = false,
    val conversionResult: ConversionResult? = null,
    val isSubmitAvailable: Boolean? = null,
    val error: String? = null,
)

data class BalanceItem(
    val currency: String,
    val formatted: String
)
