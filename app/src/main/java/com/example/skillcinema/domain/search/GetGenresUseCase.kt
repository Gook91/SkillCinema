package com.example.skillcinema.domain.search

import com.example.skillcinema.data.RepositoryFilms
import javax.inject.Inject

class GetGenresUseCase @Inject constructor(
    private val repositoryFilms: RepositoryFilms
) {
    suspend fun execute(): List<String> = repositoryFilms.getGenres().map { it.genre }
}