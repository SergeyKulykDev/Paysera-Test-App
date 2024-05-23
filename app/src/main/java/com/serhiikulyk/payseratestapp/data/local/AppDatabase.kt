package com.serhiikulyk.payseratestapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.serhiikulyk.payseratestapp.data.local.converter.Converters
import com.serhiikulyk.payseratestapp.data.local.dao.BalancesDao
import com.serhiikulyk.payseratestapp.data.local.dao.CurrencyRatesDao
import com.serhiikulyk.payseratestapp.data.local.model.BalanceEntity
import com.serhiikulyk.payseratestapp.data.local.model.CurrencyRatesEntity

@Database(entities = [BalanceEntity::class, CurrencyRatesEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun balancesDao(): BalancesDao

    abstract fun currencyRatesDao(): CurrencyRatesDao


}
