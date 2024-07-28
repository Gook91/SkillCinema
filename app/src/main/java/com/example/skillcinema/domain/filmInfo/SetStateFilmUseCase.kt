package com.example.skillcinema.domain.filmInfo

import com.example.skillcinema.data.RepositoryCollections
import javax.inject.Inject

class SetStateFilmUseCase @Inject constructor(
    private val repositoryCollections: RepositoryCollections
) {
    suspend fun execute(
        kinopoiskId: Int,
        isLiked: Boolean? = null,
        isWatchLater: Boolean? = null,
        isViewed: Boolean? = null
    ) {
        isLiked?.let { repositoryCollections.updateFilmIsLikedState(kinopoiskId, isLiked) }
        isWatchLater?.let {
            repositoryCollections.updateFilmIsWatchLaterState(
                kinopoiskId,
                isWatchLater
            )
        }
        isViewed?.let { repositoryCollections.updateFilmIsViewedState(kinopoiskId, isViewed) }
    }
}