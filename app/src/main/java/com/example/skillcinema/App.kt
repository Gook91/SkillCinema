package com.example.skillcinema

import android.app.Application
import com.example.skillcinema.di.AppComponent
import com.example.skillcinema.di.DaggerAppComponent
import com.example.skillcinema.di.DatabaseModule
import com.example.skillcinema.di.SharedPreferencesModule
import com.google.firebase.crashlytics.FirebaseCrashlytics

class App : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .databaseModule(DatabaseModule(this))
            .sharedPreferencesModule(SharedPreferencesModule(this))
            .build()
    }
}