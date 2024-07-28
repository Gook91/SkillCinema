package com.example.skillcinema.domain.filmInfo

import com.example.skillcinema.data.RepositoryFilms
import javax.inject.Inject

class GetFilmNameUseCase @Inject constructor(
    private val repositoryFilms: RepositoryFilms
) {
    suspend fun execute(kinopoiskId: Int): String {
        val film = repositoryFilms.getFilmInfo(kinopoiskId)
        return film.nameRu ?: film.nameEn ?: film.nameOriginal ?: ""
    }
}