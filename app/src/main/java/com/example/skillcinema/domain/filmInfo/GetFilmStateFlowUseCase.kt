package com.example.skillcinema.domain.filmInfo

import com.example.skillcinema.data.RepositoryFilms
import com.example.skillcinema.entity.film.FilmState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFilmStateFlowUseCase @Inject constructor(
    private val repositoryFilms: RepositoryFilms
) {
    fun execute(kinopoiskId: Int): Flow<FilmState> = repositoryFilms.getFilmStateFlow(kinopoiskId)
}