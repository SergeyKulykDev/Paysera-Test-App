package com.serhiikulyk.payseratestapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.serhiikulyk.payseratestapp.data.local.model.CurrencyRatesEntity

@Dao
interface CurrencyRatesDao {

    @Query("SELECT * FROM currency_rates WHERE date = :date")
    suspend fun get(date: String): CurrencyRatesEntity?

    @Upsert
    suspend fun insert(entity: CurrencyRatesEntity)

    @Query("DELETE FROM currency_rates")
    suspend fun deleteAll()

    @Transaction
    suspend fun dropAndCache(currencyRatesEntity: CurrencyRatesEntity) {
        deleteAll()
        insert(currencyRatesEntity)
    }
}