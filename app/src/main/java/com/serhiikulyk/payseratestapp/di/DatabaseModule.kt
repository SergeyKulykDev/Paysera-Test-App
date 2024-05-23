package com.serhiikulyk.payseratestapp.di

import android.content.Context
import androidx.room.Room
import com.serhiikulyk.payseratestapp.const.DB_NAME
import com.serhiikulyk.payseratestapp.data.local.AppDatabase
import com.serhiikulyk.payseratestapp.data.local.dao.BalancesDao
import com.serhiikulyk.payseratestapp.data.local.dao.CurrencyRatesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            DB_NAME
        ).build()
    }

    @Provides
    fun provideBalancesDao(database: AppDatabase): BalancesDao {
        return database.balancesDao()
    }

    @Provides
    fun provideCurrencyRatesDao(database: AppDatabase): CurrencyRatesDao {
        return database.currencyRatesDao()
    }

}
