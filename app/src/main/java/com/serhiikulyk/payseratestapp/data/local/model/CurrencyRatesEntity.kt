package com.serhiikulyk.payseratestapp.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency_rates")
data class CurrencyRatesEntity(
    @PrimaryKey
    @ColumnInfo("date")
    val date: String,

    @ColumnInfo("base")
    val base: String,

    @ColumnInfo("rates")
    var rates: HashMap<String, Double> = hashMapOf()

)
