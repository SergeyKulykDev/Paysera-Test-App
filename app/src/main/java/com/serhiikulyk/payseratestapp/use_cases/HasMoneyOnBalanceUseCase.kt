package com.serhiikulyk.payseratestapp.use_cases

import com.serhiikulyk.payseratestapp.data.local.model.BalanceEntity
import com.serhiikulyk.payseratestapp.data.prefs.Prefs
import com.serhiikulyk.payseratestapp.data.repository.BalancesRepository
import javax.inject.Inject

class HasMoneyOnBalanceUseCase @Inject constructor(
    private val balancesRepository: BalancesRepository,
    private val prefs: Prefs
) {
    suspend operator fun invoke(
        amount: Double,
        sellCurrency: String
    ): Boolean {
        var sellBalanceEntity = balancesRepository.getBalance(sellCurrency)
        if (sellBalanceEntity == null) {
            sellBalanceEntity = BalanceEntity(sellCurrency, 0.0)
        }

        if (sellBalanceEntity.amount == 0.0 || sellBalanceEntity.amount < amount) {
            return false
        }

        var commission = 0.0
        if (prefs.freeConversions <= 0) {
            commission = amount * ConvertCurrencyUseCase.DEFAULT_COMMISSION_RATE
        }

        val sellAmountWithCommission = sellBalanceEntity.amount - amount - commission
        return sellAmountWithCommission <= sellBalanceEntity.amount
    }

}
