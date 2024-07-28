package com.example.skillcinema.domain.userCollections

import com.example.skillcinema.data.RepositoryCollections
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCountInterestedFilmsUseCase @Inject constructor(
    private val repositoryCollections: RepositoryCollections
) {
    fun execute(): Flow<Int> = repositoryCollections.getCountInterestedFilms()
}