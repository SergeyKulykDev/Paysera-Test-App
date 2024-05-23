package com.serhiikulyk.payseratestapp.data.remote.model

import com.google.gson.annotations.SerializedName
import com.serhiikulyk.payseratestapp.const.CURRENCY_EUR
import com.serhiikulyk.payseratestapp.data.local.model.CurrencyRatesEntity
import com.serhiikulyk.payseratestapp.extensions.now
import com.serhiikulyk.payseratestapp.extensions.toFormattedString

data class NetworkCurrencyRates(

    @SerializedName("base") var base: String? = null,
    @SerializedName("date") var date: String? = null,
    @SerializedName("rates") var rates: HashMap<String, Double>? = null

)

fun NetworkCurrencyRates.toCurrencyEntity() = CurrencyRatesEntity(
    date = date ?: now.toFormattedString(),
    base = base ?: CURRENCY_EUR,
    rates = rates ?: hashMapOf()
)

