package com.serhiikulyk.payseratestapp.di

import com.serhiikulyk.payseratestapp.synchronizer.CustomEventBus
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class EventBusModule {

    @Provides
    @Singleton
    fun provideEventBus() = CustomEventBus()

}