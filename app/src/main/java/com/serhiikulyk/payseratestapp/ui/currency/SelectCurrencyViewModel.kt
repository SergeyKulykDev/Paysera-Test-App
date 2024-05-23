package com.serhiikulyk.payseratestapp.ui.currency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serhiikulyk.payseratestapp.data.repository.CurrencyRatesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectCurrencyViewModel @Inject constructor(
    private val currencyRatesRepository: CurrencyRatesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<List<CurrencyItem>>(emptyList())
    val uiState = _uiState.asStateFlow()

    init {
        initialize()
    }

    private fun initialize() {
        viewModelScope.launch {
            val currencyRatesEntity = currencyRatesRepository.getTodayCurrencyRate()
            _uiState.update {
                currencyRatesEntity?.rates
                    ?.map { (key, _) -> CurrencyItem(key) }
                    ?.sortedBy { it.code }
                    .orEmpty()
            }
        }
    }
}

data class CurrencyItem(
    val code: String
)