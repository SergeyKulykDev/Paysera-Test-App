package com.serhiikulyk.payseratestapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.serhiikulyk.payseratestapp.data.local.model.BalanceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BalancesDao {

    @Query("SELECT * FROM balances WHERE currency = :currency")
    suspend fun get(currency: String): BalanceEntity?

    @Query("SELECT * FROM balances")
    suspend fun getAll(): List<BalanceEntity>

    @Query("SELECT * FROM balances")
    fun getStream(): Flow<List<BalanceEntity>>

    @Upsert
    suspend fun upsert(balanceEntity: BalanceEntity)

    @Upsert
    suspend fun upsertAll(balanceEntities: List<BalanceEntity>)

}