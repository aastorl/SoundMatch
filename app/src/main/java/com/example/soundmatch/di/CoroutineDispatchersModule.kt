package com.example.soundmatch.di

import com.example.musify.di.IODispatcher
import com.example.musify.di.MainDispatcher
import com.fasterxml.jackson.databind.annotation.JsonAppend
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Qualifier
annotation class MainDispatcher

@Qualifier
annotation class IODispatcher

@Qualifier
annotation class DefaultDispatcher

@Module
@InstallIn (ActivityRetainedComponent::class)
object CoroutineDispatcherModule {
    @Provides
    @DefaultDispatcher
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @IODispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @MainDispatcher
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}