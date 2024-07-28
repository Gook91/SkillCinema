package com.example.skillcinema.domain.filmInfo

import com.example.skillcinema.data.RepositoryFilms
import com.example.skillcinema.entity.film.types.GalleryType
import javax.inject.Inject

class GetGalleryTypesUseCase @Inject constructor(
    private val repositoryFilms: RepositoryFilms
) {
    suspend fun execute(kinopoiskId: Int): Map<GalleryType, Int> =
        repositoryFilms.getGalleryTypesWithCountFromFilm(kinopoiskId)
}