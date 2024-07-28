package com.example.skillcinema.domain.previews

import com.example.skillcinema.data.RepositoryFilms
import com.example.skillcinema.entity.response.ResponseList
import com.example.skillcinema.entity.film.Film
import javax.inject.Inject

class GetPreviewSimilar @Inject constructor(private val repositoryFilms: RepositoryFilms) {
    suspend fun execute(kinopoiskId: Int): ResponseList<Film> = repositoryFilms.getSimilarFilms(kinopoiskId)
}