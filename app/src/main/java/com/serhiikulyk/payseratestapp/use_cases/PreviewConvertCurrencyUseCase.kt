package com.serhiikulyk.payseratestapp.use_cases

import com.serhiikulyk.payseratestapp.data.repository.CurrencyRatesRepository
import javax.inject.Inject

class PreviewConvertCurrencyUseCase @Inject constructor(
    private val currencyRatesRepository: CurrencyRatesRepository
) {
    suspend operator fun invoke(
        amount: Double,
        sellCurrency: String,
        buyCurrency: String
    ): Double {
        val currencyRatesEntity = currencyRatesRepository.getTodayCurrencyRate()
            ?: throw IllegalArgumentException("No currency rates for today")

        return convertCurrency(
            amount,
            sellCurrency,
            buyCurrency,
            currencyRatesEntity.rates
        )
    }

}

