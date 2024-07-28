package com.example.skillcinema.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.skillcinema.data.db.dto.collections.CollectionFilmDto
import com.example.skillcinema.data.db.dto.collections.CollectionLocalDto
import com.example.skillcinema.data.db.dto.film.FilmLocalDto
import com.example.skillcinema.data.db.dto.film.EpisodeLocalDto
import com.example.skillcinema.data.db.dto.filmState.InterestedFilms
import com.example.skillcinema.data.db.dto.filmState.LikedFilms
import com.example.skillcinema.data.db.dto.filmState.ViewedFilms
import com.example.skillcinema.data.db.dto.filmState.WatchLaterFilms
import com.example.skillcinema.data.db.dto.staff.StaffLocalDto

@Database(
    entities = [FilmLocalDto::class, EpisodeLocalDto::class,
        StaffLocalDto::class,
        CollectionLocalDto::class, CollectionFilmDto::class,
        ViewedFilms::class, LikedFilms::class, WatchLaterFilms::class, InterestedFilms::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun filmsDao(): FilmsDao

    abstract fun staffDao(): StaffDao

    abstract fun filmStateDao(): FilmStateDao

    abstract fun userCollectionsDao(): UserCollectionDao

    companion object {
        const val DB_NAME = "db"
    }
}