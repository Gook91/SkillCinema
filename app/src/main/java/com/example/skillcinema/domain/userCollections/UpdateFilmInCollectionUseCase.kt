package com.example.skillcinema.domain.userCollections

import com.example.skillcinema.data.RepositoryCollections
import com.example.skillcinema.entity.collections.TypeCollection
import javax.inject.Inject

class UpdateFilmInCollectionUseCase @Inject constructor(
    private val repositoryCollections: RepositoryCollections
) {
    suspend fun execute(
        typeCollection: TypeCollection,
        kinopoiskId: Int,
        isFilmInCollection: Boolean
    ) {
        when (typeCollection) {
            TypeCollection.Liked -> repositoryCollections.updateFilmIsLikedState(
                kinopoiskId,
                isFilmInCollection
            )

            TypeCollection.Viewed -> repositoryCollections.updateFilmIsViewedState(
                kinopoiskId,
                isFilmInCollection
            )

            TypeCollection.WatchLater -> repositoryCollections.updateFilmIsWatchLaterState(
                kinopoiskId,
                isFilmInCollection
            )

            is TypeCollection.UserCollection -> repositoryCollections.updateFilmInUserCollection(
                typeCollection.id,
                kinopoiskId,
                isFilmInCollection
            )
            // В коллекцию "Интересовались" фильмы только добавляются при просмотре,
            // текущая бизнес-логика заключается в том, что эта коллекция очищается только полностью
            TypeCollection.Interested -> repositoryCollections.addFilmToInterested(kinopoiskId)
        }
    }
}