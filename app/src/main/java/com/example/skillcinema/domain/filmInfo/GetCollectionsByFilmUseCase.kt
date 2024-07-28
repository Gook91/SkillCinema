package com.example.skillcinema.domain.filmInfo

import com.example.skillcinema.data.RepositoryCollections
import com.example.skillcinema.entity.collections.CollectionFilms
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCollectionsByFilmUseCase @Inject constructor(
    private val repositoryCollections: RepositoryCollections
) {
    fun execute(kinopoiskId: Int): Flow<List<CollectionFilms>> =
        repositoryCollections.getUserCollectionsByFilmFlow(kinopoiskId)
}