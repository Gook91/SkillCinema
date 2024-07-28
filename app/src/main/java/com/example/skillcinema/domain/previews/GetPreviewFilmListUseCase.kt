package com.example.skillcinema.domain.previews

import com.example.skillcinema.data.RepositoryFilms
import com.example.skillcinema.entity.response.ResponseList
import com.example.skillcinema.entity.film.Film
import com.example.skillcinema.entity.film.types.TypeFilm
import javax.inject.Inject

class GetPreviewFilmListUseCase @Inject constructor(
    private val repositoryFilms: RepositoryFilms
) {
    suspend fun execute(
        country: String? = null,
        genre: String? = null,
        typeFilm: TypeFilm? = null,
    ): ResponseList<Film> = repositoryFilms.getFilmsByParams(
        country = country,
        genre = genre,
        typeFilm = typeFilm
    )
}