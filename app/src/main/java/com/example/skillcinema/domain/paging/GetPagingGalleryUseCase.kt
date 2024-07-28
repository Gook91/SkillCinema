package com.example.skillcinema.domain.paging

import com.example.skillcinema.data.RepositoryFilms
import com.example.skillcinema.data.pagging.GalleryPagingSource
import com.example.skillcinema.entity.film.types.GalleryType
import javax.inject.Inject

class GetPagingGalleryUseCase @Inject constructor(
    private val repositoryFilms: RepositoryFilms
) {
    fun execute(kinopoiskId: Int, galleryType: GalleryType): GalleryPagingSource {
        val factory: (Int, GalleryType) -> GalleryPagingSource = { id, type ->
            GalleryPagingSource(repositoryFilms, id, type)
        }
        return factory(kinopoiskId, galleryType)
    }
}