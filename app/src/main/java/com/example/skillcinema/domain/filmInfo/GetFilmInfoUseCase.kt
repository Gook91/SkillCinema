package com.example.skillcinema.domain.filmInfo

import com.example.skillcinema.data.RepositoryCollections
import com.example.skillcinema.data.RepositoryFilms
import com.example.skillcinema.entity.film.Film
import javax.inject.Inject

class GetFilmInfoUseCase @Inject constructor(
    private val repositoryFilms: RepositoryFilms,
    private val repositoryCollections: RepositoryCollections
) {
    suspend fun execute(kinopoiskId: Int): Film {
        repositoryCollections.addFilmToInterested(kinopoiskId)
        return repositoryFilms.getFilmInfo(kinopoiskId)
    }
}