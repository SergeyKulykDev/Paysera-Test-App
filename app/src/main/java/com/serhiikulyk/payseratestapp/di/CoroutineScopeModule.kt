package com.serhiikulyk.payseratestapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object CoroutineScopeModule {

    @IoScope
    @Provides
    fun provideIoScope(@IoDispatcher dispatcher: CoroutineDispatcher) =
        CoroutineScope(SupervisorJob() + dispatcher)

    @ExternalScope
    @Provides
    fun provideExternalScope(@DefaultDispatcher dispatcher: CoroutineDispatcher) =
        CoroutineScope(SupervisorJob() + dispatcher)

    @UiScope
    @Provides
    fun provideUiScope(@MainDispatcher dispatcher: CoroutineDispatcher) =
        CoroutineScope(SupervisorJob() + dispatcher)

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ExternalScope

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UiScope


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoScope