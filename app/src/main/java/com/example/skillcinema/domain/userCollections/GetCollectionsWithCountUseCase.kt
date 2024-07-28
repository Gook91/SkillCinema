package com.example.skillcinema.domain.userCollections

import com.example.skillcinema.data.RepositoryCollections
import com.example.skillcinema.entity.collections.CollectionFilms
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCollectionsWithCountUseCase @Inject constructor(
    private val repositoryCollections: RepositoryCollections
) {
    fun execute(): Flow<List<CollectionFilms>> =
        repositoryCollections.getUserCollectionsFlow()
}