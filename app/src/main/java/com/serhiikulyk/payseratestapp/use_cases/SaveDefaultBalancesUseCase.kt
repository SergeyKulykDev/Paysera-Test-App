package com.serhiikulyk.payseratestapp.use_cases

import com.serhiikulyk.payseratestapp.const.CURRENCY_EUR
import com.serhiikulyk.payseratestapp.const.CURRENCY_PLN
import com.serhiikulyk.payseratestapp.const.CURRENCY_UAH
import com.serhiikulyk.payseratestapp.const.CURRENCY_USD
import com.serhiikulyk.payseratestapp.data.local.model.BalanceEntity
import com.serhiikulyk.payseratestapp.data.repository.BalancesRepository
import com.serhiikulyk.payseratestapp.di.ExternalScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class SaveDefaultBalancesUseCase @Inject constructor(
    private val balancesRepository: BalancesRepository,
    @ExternalScope private val externalScope: CoroutineScope
) {

    operator fun invoke() {
        externalScope.launch {
            if (!balancesRepository.hasBalances()) {

                val eurBalance = BalanceEntity(CURRENCY_EUR, 1000.0)
                val usdBalance = BalanceEntity(CURRENCY_USD, 0.0)
                val uahBalance = BalanceEntity(CURRENCY_UAH, 0.0)
                val plnBalance = BalanceEntity(CURRENCY_PLN, 0.0)

                val balanceEntities = listOf(
                    eurBalance,
                    usdBalance,
                    uahBalance,
                    plnBalance
                )

                balancesRepository.saveBalances(balanceEntities)
            }
        }
    }

}