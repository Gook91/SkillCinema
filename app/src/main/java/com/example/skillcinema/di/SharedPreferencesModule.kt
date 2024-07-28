package com.example.skillcinema.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides

@Module
class SharedPreferencesModule(
    private val context: Context
) {
    @Provides
    fun provideSharedPreferences(): SharedPreferences =
        context.getSharedPreferences(SETTINGS_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val SETTINGS_SHARED_PREFERENCES_NAME = "settings"
    }
}