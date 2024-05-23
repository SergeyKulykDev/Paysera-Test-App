package com.serhiikulyk.payseratestapp.data.repository

import com.serhiikulyk.payseratestapp.data.local.dao.BalancesDao
import com.serhiikulyk.payseratestapp.data.local.model.BalanceEntity
import javax.inject.Inject

class BalancesRepository @Inject constructor(
    private val balancesDao: BalancesDao
) {

    suspend fun hasBalances() = balancesDao.getAll().isNotEmpty()

    suspend fun getBalance(currencyCode: String) = balancesDao.get(currencyCode)

    fun getBalancesStream() = balancesDao.getStream()

    suspend fun saveBalance(balanceEntity: BalanceEntity) = balancesDao.upsert(balanceEntity)

    suspend fun saveBalances(balanceEntities: List<BalanceEntity>) = balancesDao.upsertAll(balanceEntities)

}