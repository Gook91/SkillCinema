package com.example.skillcinema.di

import com.example.skillcinema.data.MemoryStorage
import dagger.Module
import dagger.Provides

@Module
class MemoryStorageModule {
    @Provides
    fun provideMemoryStorage(): MemoryStorage = MemoryStorage
}