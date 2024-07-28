package com.example.skillcinema.data

import com.example.skillcinema.data.db.FilmStateDao
import com.example.skillcinema.data.db.UserCollectionDao
import com.example.skillcinema.data.db.dto.collections.CollectionFilmDto
import com.example.skillcinema.data.db.dto.collections.CollectionLocalDto
import com.example.skillcinema.data.db.dto.filmState.InterestedFilms
import com.example.skillcinema.data.db.dto.filmState.LikedFilms
import com.example.skillcinema.data.db.dto.filmState.ViewedFilms
import com.example.skillcinema.data.db.dto.filmState.WatchLaterFilms
import com.example.skillcinema.entity.collections.TypeCollection
import com.example.skillcinema.entity.collections.CollectionFilms
import com.example.skillcinema.entity.film.Film
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RepositoryCollections @Inject constructor(
    private val filmStateDao: FilmStateDao,
    private val userCollectionDao: UserCollectionDao

) {

    // Создание коллекции

    suspend fun addNewCollection(newCollectionName: String) {
        userCollectionDao.addCollection(CollectionLocalDto(newCollectionName))
    }

    // Получение информации о коллекциях

    fun getUserCollectionsFlow(): Flow<List<CollectionFilms>> {
        val likedFilmFlow: Flow<List<CollectionFilms>> =
            filmStateDao.getCountLikedFilms().map { count ->
                listOf(
                    CollectionFilms(
                        type = TypeCollection.Liked,
                        countFilms = count
                    )
                )
            }
        val watchLaterFilmFlow: Flow<List<CollectionFilms>> =
            filmStateDao.getCountWatchLaterFilms().map { count ->
                listOf(
                    CollectionFilms(
                        type = TypeCollection.WatchLater,
                        countFilms = count
                    )
                )
            }
        val userCollectionsFlow: Flow<List<CollectionFilms>> =
            userCollectionDao.getUserCollections().map { userCollections ->
                userCollections.map { it.toUserCollection() }
            }
        val collectionsFlow: Flow<List<CollectionFilms>> =
            combine(likedFilmFlow, watchLaterFilmFlow, userCollectionsFlow) { liked, later, user ->
                liked + later + user
            }
        return collectionsFlow
    }

    fun getUserCollectionsByFilmFlow(kinopoiskId: Int): Flow<List<CollectionFilms>> {
        val allCollectionsFlow = getUserCollectionsFlow()
        val userCollectionsWithFilmFlow = userCollectionDao.getCollectionsByFilm(kinopoiskId)
        val isLikedFilmFlow = filmStateDao.isLikedFilm(kinopoiskId)
        val isWatchLaterFilmFlow = filmStateDao.isWatchLaterFilm(kinopoiskId)

        val collectionsByFilmFlow: Flow<List<CollectionFilms>> =
            combine(
                allCollectionsFlow,
                userCollectionsWithFilmFlow,
                isLikedFilmFlow,
                isWatchLaterFilmFlow
            ) { rawList, userCollections, isLiked, isWatchLater ->
                rawList.onEach { collection ->
                    collection.isMovieInCollection = when (collection.type) {
                        TypeCollection.Liked -> isLiked
                        TypeCollection.WatchLater -> isWatchLater
                        is TypeCollection.UserCollection -> collection.type.id in userCollections
                        else -> null

                    }
                }
            }
        return collectionsByFilmFlow
    }

    fun getCountViewedFilms(): Flow<Int> = filmStateDao.getCountViewedFilms()

    fun getCountInterestedFilms(): Flow<Int> = filmStateDao.getCountInterestedFilms()

    // Получение фильмов из коллекций

    fun getLikedFilms(): Flow<List<Film>> =
        filmStateDao.getLikedFilms().map { films -> films.map { it.toFilm() } }

    fun getWatchLaterFilms(): Flow<List<Film>> =
        filmStateDao.getWatchLaterFilms().map { films -> films.map { it.toFilm() } }

    fun getViewedFilms(): Flow<List<Film>> =
        filmStateDao.getViewedFilms().map { films -> films.map { it.toFilm() } }

    fun getInterestedFilms(): Flow<List<Film>> =
        filmStateDao.getInterestedFilms().map { films -> films.map { it.toFilm() } }

    fun getFilmsFromUserCollection(collectionId: Int): Flow<List<Film>> =
        userCollectionDao.getFilmsFromCollection(collectionId)
            .map { films -> films.map { it.toFilm() } }

    // Добавление фильма в различные коллекции

    suspend fun updateFilmIsViewedState(kinopoiskId: Int, isViewed: Boolean) {
        if (isViewed) filmStateDao.addFilmToViewed(ViewedFilms(kinopoiskId = kinopoiskId))
        else filmStateDao.deleteFilmFromViewed(kinopoiskId)
    }

    suspend fun updateFilmIsWatchLaterState(kinopoiskId: Int, isWatchLater: Boolean) {
        if (isWatchLater) filmStateDao.addFilmToWatchLater(WatchLaterFilms(kinopoiskId = kinopoiskId))
        else filmStateDao.deleteFilmFromWatchLater(kinopoiskId)
    }

    suspend fun updateFilmIsLikedState(kinopoiskId: Int, isLiked: Boolean) {
        if (isLiked) filmStateDao.addFilmToLiked(LikedFilms(kinopoiskId = kinopoiskId))
        else filmStateDao.deleteFilmFromLiked(kinopoiskId)
    }

    suspend fun addFilmToInterested(kinopoiskId: Int) {
        filmStateDao.addFilmToInterested(InterestedFilms(kinopoiskId = kinopoiskId))
    }

    suspend fun updateFilmInUserCollection(
        collectionId: Int,
        kinopoiskId: Int,
        isFilmInCollection: Boolean
    ) {
        val collectionFilmDto = CollectionFilmDto(collectionId, kinopoiskId)
        if (isFilmInCollection) userCollectionDao.addFilmInCollection(collectionFilmDto)
        else userCollectionDao.deleteFilmFromCollection(collectionFilmDto)
    }

    // Удаление и очистка коллекций

    suspend fun clearViewedFilms() = filmStateDao.clearViewedFilms()

    suspend fun clearInterestedFilms() = filmStateDao.clearInterestedFilms()

    suspend fun deleteUserCollection(collectionId: Int) =
        userCollectionDao.deleteUserCollection(collectionId)
}