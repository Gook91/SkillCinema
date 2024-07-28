package com.example.skillcinema.domain.filmInfo

import com.example.skillcinema.data.RepositoryFilms
import com.example.skillcinema.entity.film.Season
import javax.inject.Inject

class GetSeasonsAndSeriesUseCase @Inject constructor(
    private val repositoryFilms: RepositoryFilms
) {
    suspend fun execute(kinopoiskId: Int): List<Season> =
        repositoryFilms.getSeasonsAndSeries(kinopoiskId)
}