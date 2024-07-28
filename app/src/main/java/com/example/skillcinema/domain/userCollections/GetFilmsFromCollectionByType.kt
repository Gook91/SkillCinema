package com.example.skillcinema.domain.userCollections

import com.example.skillcinema.data.RepositoryCollections
import com.example.skillcinema.entity.collections.TypeCollection
import com.example.skillcinema.entity.film.Film
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFilmsFromCollectionByType @Inject constructor(
    private val repositoryCollections: RepositoryCollections
) {
    fun execute(typeCollection: TypeCollection): Flow<List<Film>> {
        return when (typeCollection) {
            TypeCollection.Interested -> repositoryCollections.getInterestedFilms()
            TypeCollection.Liked -> repositoryCollections.getLikedFilms()
            TypeCollection.Viewed -> repositoryCollections.getViewedFilms()
            TypeCollection.WatchLater -> repositoryCollections.getWatchLaterFilms()
            is TypeCollection.UserCollection -> repositoryCollections.getFilmsFromUserCollection(
                typeCollection.id
            )
        }
    }
}