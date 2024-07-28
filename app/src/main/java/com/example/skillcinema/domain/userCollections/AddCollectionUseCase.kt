package com.example.skillcinema.domain.userCollections

import com.example.skillcinema.data.RepositoryCollections
import javax.inject.Inject

class AddCollectionUseCase @Inject constructor(
    private val repositoryCollections: RepositoryCollections
) {
    suspend fun execute(newCollectionName: String) = repositoryCollections.addNewCollection(newCollectionName)
}