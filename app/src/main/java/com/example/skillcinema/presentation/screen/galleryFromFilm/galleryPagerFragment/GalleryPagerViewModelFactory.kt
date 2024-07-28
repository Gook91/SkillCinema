package com.example.skillcinema.presentation.screen.galleryFromFilm.galleryPagerFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skillcinema.entity.film.types.GalleryType
import java.lang.IllegalArgumentException

class GalleryPagerViewModelFactory(
    private val kinopoiskId: Int,
    private val galleryType: GalleryType,
    private val create: (Int, GalleryType) -> ViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(GalleryPagerViewModel::class.java))
            return create(kinopoiskId, galleryType) as T
        throw IllegalArgumentException()
    }
}