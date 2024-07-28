package com.example.skillcinema.domain.userCollections

import com.example.skillcinema.data.RepositoryCollections
import javax.inject.Inject

class ClearInterestedHistoryUseCase @Inject constructor(
    private val repositoryCollections: RepositoryCollections
) {
    suspend fun execute() = repositoryCollections.clearInterestedFilms()
}