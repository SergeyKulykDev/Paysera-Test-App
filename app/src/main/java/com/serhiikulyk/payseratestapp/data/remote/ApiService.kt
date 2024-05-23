package com.serhiikulyk.payseratestapp.data.remote

import com.serhiikulyk.payseratestapp.data.remote.model.NetworkCurrencyRates
import retrofit2.http.GET

interface ApiService {

    @GET("tasks/api/currency-exchange-rates")
    suspend fun getCurrencyRates(): NetworkCurrencyRates

}