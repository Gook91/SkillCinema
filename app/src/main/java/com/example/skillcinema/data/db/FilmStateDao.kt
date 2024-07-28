package com.example.skillcinema.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.skillcinema.data.db.dto.film.FilmLocalDto
import com.example.skillcinema.data.db.dto.filmState.InterestedFilms
import com.example.skillcinema.data.db.dto.filmState.LikedFilms
import com.example.skillcinema.data.db.dto.filmState.ViewedFilms
import com.example.skillcinema.data.db.dto.filmState.WatchLaterFilms
import kotlinx.coroutines.flow.Flow

@Dao
interface FilmStateDao {

    // Информация о количестве фильмов с разными статусами

    @Query("SELECT COUNT(kinopoisk_id) FROM viewed_films")
    fun getCountViewedFilms(): Flow<Int>

    @Query("SELECT COUNT(kinopoisk_id) FROM liked_films")
    fun getCountLikedFilms(): Flow<Int>

    @Query("SELECT COUNT(kinopoisk_id) FROM watch_later_films")
    fun getCountWatchLaterFilms(): Flow<Int>

    @Query("SELECT COUNT(kinopoisk_id) FROM interested_films")
    fun getCountInterestedFilms(): Flow<Int>

    // Списки фильмов с разными статусами

    @Transaction
    @Query(
        "SELECT F.* FROM films F " +
                "JOIN liked_films L ON F.kinopoisk_id = L.kinopoisk_id"
    )
    fun getLikedFilms(): Flow<List<FilmLocalDto>>

    @Transaction
    @Query(
        "SELECT F.* FROM films F " +
                "JOIN watch_later_films W ON F.kinopoisk_id = W.kinopoisk_id "
    )
    fun getWatchLaterFilms(): Flow<List<FilmLocalDto>>

    @Transaction
    @Query(
        "SELECT F.* FROM films F " +
                "JOIN viewed_films V ON F.kinopoisk_id = V.kinopoisk_id"
    )
    fun getViewedFilms(): Flow<List<FilmLocalDto>>

    @Query("SELECT kinopoisk_id FROM viewed_films")
    suspend fun getIdViewedFilms(): List<Int>

    @Transaction
    @Query(
        "SELECT F.* FROM films F " +
                "JOIN interested_films I ON F.kinopoisk_id = I.kinopoisk_id " +
                "ORDER BY I.id DESC"
    )
    fun getInterestedFilms(): Flow<List<FilmLocalDto>>

    // Получение статуса у фильма

    @Query("SELECT EXISTS (SELECT 1 FROM viewed_films WHERE kinopoisk_id = :kinopoiskId)")
    fun isViewedFilm(kinopoiskId: Int): Flow<Boolean>

    @Query("SELECT EXISTS (SELECT 1 FROM liked_films WHERE kinopoisk_id = :kinopoiskId)")
    fun isLikedFilm(kinopoiskId: Int): Flow<Boolean>

    @Query("SELECT EXISTS (SELECT 1 FROM watch_later_films WHERE kinopoisk_id = :kinopoiskId)")
    fun isWatchLaterFilm(kinopoiskId: Int): Flow<Boolean>

    // Добавление статуса для фильма

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFilmToViewed(viewedFilms: ViewedFilms)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFilmToLiked(likedFilms: LikedFilms)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFilmToWatchLater(watchLaterFilms: WatchLaterFilms)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFilmToInterested(interestedFilms: InterestedFilms)

    // Удаление статуса у фильма

    @Query("DELETE FROM viewed_films WHERE kinopoisk_id = :kinopoiskId")
    suspend fun deleteFilmFromViewed(kinopoiskId: Int)

    @Query("DELETE FROM liked_films WHERE kinopoisk_id = :kinopoiskId")
    suspend fun deleteFilmFromLiked(kinopoiskId: Int)

    @Query("DELETE FROM watch_later_films WHERE kinopoisk_id = :kinopoiskId")
    suspend fun deleteFilmFromWatchLater(kinopoiskId: Int)

    // Очистка списков

    @Query("DELETE FROM viewed_films")
    suspend fun clearViewedFilms()

    @Query("DELETE FROM interested_films")
    suspend fun clearInterestedFilms()
}