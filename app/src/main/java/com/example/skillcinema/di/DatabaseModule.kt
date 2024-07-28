package com.example.skillcinema.di

import android.app.Application
import androidx.room.Room
import com.example.skillcinema.data.db.AppDatabase
import com.example.skillcinema.data.db.FilmStateDao
import com.example.skillcinema.data.db.FilmsDao
import com.example.skillcinema.data.db.StaffDao
import com.example.skillcinema.data.db.UserCollectionDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule(
    private val application: Application
) {
    @Singleton
    private val appDatabase: AppDatabase by lazy {
        Room.databaseBuilder(
            application.applicationContext,
            AppDatabase::class.java,
            AppDatabase.DB_NAME
        ).build()
    }

    @Provides
    fun provideFilmsDao(): FilmsDao = appDatabase.filmsDao()

    @Provides
    fun provideStaffDao(): StaffDao = appDatabase.staffDao()

    @Provides
    fun provideFilmStateDao(): FilmStateDao = appDatabase.filmStateDao()

    @Provides
    fun provideUserCollectionDao(): UserCollectionDao = appDatabase.userCollectionsDao()
}