package com.serhiikulyk.payseratestapp.use_cases

import com.serhiikulyk.payseratestapp.data.local.model.BalanceEntity
import com.serhiikulyk.payseratestapp.data.prefs.Prefs
import com.serhiikulyk.payseratestapp.data.repository.BalancesRepository
import com.serhiikulyk.payseratestapp.data.repository.CurrencyRatesRepository
import javax.inject.Inject

class ConvertCurrencyUseCase @Inject constructor(
    private val balancesRepository: BalancesRepository,
    private val currencyRatesRepository: CurrencyRatesRepository,
    private val prefs: Prefs
) {
    suspend operator fun invoke(
        amount: Double,
        sellCurrency: String,
        buyCurrency: String
    ): ConversionResult {
        var sellBalanceEntity = balancesRepository.getBalance(sellCurrency)
        if (sellBalanceEntity == null) {
            sellBalanceEntity = BalanceEntity(sellCurrency, 0.0)
        }

        var buyBalanceEntity = balancesRepository.getBalance(buyCurrency)
        if (buyBalanceEntity == null) {
            buyBalanceEntity = BalanceEntity(buyCurrency, 0.0)
        }

        val currencyRatesEntity = currencyRatesRepository.getTodayCurrencyRate()
            ?: throw IllegalArgumentException("No currency rates for today")

        if (sellBalanceEntity.amount < amount) {
            throw IllegalArgumentException("Insufficient balance")
        }

        var commission = 0.0
        if (prefs.freeConversions <= 1) {
            commission = amount * DEFAULT_COMMISSION_RATE
        } else {
            prefs.freeConversions--
        }

        val convertedAmount = convertCurrency(
            amount,
            sellCurrency,
            buyCurrency,
            currencyRatesEntity.rates
        )

        sellBalanceEntity.amount = sellBalanceEntity.amount - amount - commission
        buyBalanceEntity.amount += convertedAmount

        balancesRepository.saveBalance(sellBalanceEntity)
        balancesRepository.saveBalance(buyBalanceEntity)

        return ConversionResult(
            sell = amount,
            buy = convertedAmount,
            sellCurrency = sellCurrency,
            buyCurrency = buyCurrency,
            commission = commission
        )

    }

    companion object {
        const val DEFAULT_FREE_CONVERSIONS = 5
        const val DEFAULT_COMMISSION_RATE = 0.007
    }
}

data class ConversionResult(
    val sell: Double,
    val buy: Double,
    val sellCurrency: String,
    val buyCurrency: String,
    val commission: Double? = null
)


fun convertCurrency(
    amount: Double,
    fromCurrency: String,
    toCurrency: String,
    rates: Map<String, Double>
): Double {
    val fromRate = rates[fromCurrency] ?: error("Unknown fromCurrency: $fromCurrency")
    val toRate = rates[toCurrency] ?: error("Unknown toCurrency: $toCurrency")

    // Convert from the base currency to the target currency
    return (amount / fromRate) * toRate
}