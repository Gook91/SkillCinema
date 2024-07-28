package com.example.skillcinema.presentation.screen.galleryFromFilm

import com.example.skillcinema.domain.filmInfo.GetGalleryTypesUseCase
import com.example.skillcinema.entity.film.types.GalleryType
import com.example.skillcinema.presentation.screen.abstractTemplates.AbstractViewModelWithErrorChannel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class GalleryFromFilmViewModel @AssistedInject constructor(
    getGalleryTypesUseCase: GetGalleryTypesUseCase,
    @Assisted kinopoiskId: Int
) : AbstractViewModelWithErrorChannel() {

    val galleryTypesFlow = flow<Map<GalleryType, Int>?> {
        val galleryTypes = getGalleryTypesUseCase.execute(kinopoiskId)
        emit(galleryTypes)
    }.stateIn(
        viewModelScopeWithExceptionHandler,
        SharingStarted.WhileSubscribed(5_000L),
        null
    )

    @AssistedFactory
    interface Factory {
        fun create(kinopoiskId: Int): GalleryFromFilmViewModel
    }
}