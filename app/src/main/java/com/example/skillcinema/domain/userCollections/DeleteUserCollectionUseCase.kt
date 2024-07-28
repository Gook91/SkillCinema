package com.example.skillcinema.domain.userCollections

import com.example.skillcinema.data.RepositoryCollections
import javax.inject.Inject

class DeleteUserCollectionUseCase @Inject constructor(
    private val repositoryCollections: RepositoryCollections
) {
    suspend fun execute(collectionId: Int) =
        repositoryCollections.deleteUserCollection(collectionId)
}