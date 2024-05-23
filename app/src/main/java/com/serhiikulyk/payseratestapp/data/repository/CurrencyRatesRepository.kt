package com.serhiikulyk.payseratestapp.data.repository

import com.serhiikulyk.payseratestapp.data.local.dao.CurrencyRatesDao
import com.serhiikulyk.payseratestapp.data.remote.ApiService
import com.serhiikulyk.payseratestapp.data.remote.model.NetworkCurrencyRates
import com.serhiikulyk.payseratestapp.data.remote.model.toCurrencyEntity
import com.serhiikulyk.payseratestapp.extensions.now
import com.serhiikulyk.payseratestapp.extensions.toFormattedString
import javax.inject.Inject

class CurrencyRatesRepository @Inject constructor(
    private val apiService: ApiService,
    private val currencyRatesDao: CurrencyRatesDao
) {

    suspend fun fetchAndSaveCurrencyRates(): NetworkCurrencyRates {
        val networkCurrencyRatesResult = apiService.getCurrencyRates()
        currencyRatesDao.dropAndCache(networkCurrencyRatesResult.toCurrencyEntity())
        return networkCurrencyRatesResult
    }

    private suspend fun getCurrencyRate(date: String) = currencyRatesDao.get(date)

    suspend fun getTodayCurrencyRate() = getCurrencyRate(now.toFormattedString())

}