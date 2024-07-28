package com.example.skillcinema.domain.previews

import com.example.skillcinema.data.RepositoryFilms
import com.example.skillcinema.entity.response.ResponseList
import com.example.skillcinema.entity.film.Image
import javax.inject.Inject

class GetPreviewGalleryUseCase @Inject constructor(
    private val repositoryFilms: RepositoryFilms
) {
    suspend fun execute(
        kinopoiskId: Int
    ): ResponseList<Image> {
        val type = repositoryFilms.getGalleryTypesWithCountFromFilm(kinopoiskId).keys.firstOrNull()
        return if (type == null)
            ResponseList()
        else
            repositoryFilms.getImagesFromFilm(kinopoiskId, type)
    }
}