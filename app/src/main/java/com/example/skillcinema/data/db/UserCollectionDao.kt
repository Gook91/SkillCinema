package com.example.skillcinema.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.skillcinema.data.db.dto.collections.CollectionFilmDto
import com.example.skillcinema.data.db.dto.collections.CollectionLocalDto
import com.example.skillcinema.data.db.dto.collections.CollectionWithCountFilmsDto
import com.example.skillcinema.data.db.dto.film.FilmLocalDto
import kotlinx.coroutines.flow.Flow

@Dao
interface UserCollectionDao {

    // Работа с коллекцией

    @Insert
    suspend fun addCollection(collectionLocalDto: CollectionLocalDto)

    // Списки коллекций и их информация

    @Transaction
    @Query(
        "SELECT C.*, " +
                "(SELECT COUNT(CF.kinopoisk_id) FROM collection_film CF " +
                "WHERE CF.collection_id = C.collection_id) as count_films " +
                "FROM collections C "
    )
    fun getUserCollections(): Flow<List<CollectionWithCountFilmsDto>>

    // Информация о коллекциях по фильму

    @Query(
        "SELECT C.collection_id " + //TODO попробовать с CASE
                "FROM collections C " +
                "INNER JOIN collection_film CF ON C.collection_id = CF.collection_id " +
                "WHERE CF.kinopoisk_id = :kinopoiskId"
    )
    fun getCollectionsByFilm(kinopoiskId: Int): Flow<List<Int>>

    // Списки фильмов из коллекций

    @Transaction
    @Query(
        "SELECT F.* FROM films F " +
                "INNER JOIN collection_film CF ON F.kinopoisk_id = CF.kinopoisk_id " +
                "WHERE CF.collection_id = :collectionId"
    )
    fun getFilmsFromCollection(collectionId: Int): Flow<List<FilmLocalDto>>

    // Добавление и удаление фильма в коллекции
    @Insert
    suspend fun addFilmInCollection(collectionFilmDto: CollectionFilmDto)

    @Delete
    suspend fun deleteFilmFromCollection(collectionFilmDto: CollectionFilmDto)

    // Удаление коллекций и очищение списков

    @Transaction
    suspend fun deleteUserCollection(collectionId: Int) {
        deleteUserCollectionInfo(collectionId)
        deleteUserCollectionFilms(collectionId)
    }

    @Query("DELETE FROM collections WHERE collection_id = :collectionId")
    suspend fun deleteUserCollectionInfo(collectionId: Int)

    @Query("DELETE FROM collection_film WHERE collection_id = :collectionId")
    suspend fun deleteUserCollectionFilms(collectionId: Int)
}