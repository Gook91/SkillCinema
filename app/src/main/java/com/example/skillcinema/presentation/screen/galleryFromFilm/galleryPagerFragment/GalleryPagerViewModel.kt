package com.example.skillcinema.presentation.screen.galleryFromFilm.galleryPagerFragment

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.skillcinema.domain.paging.GetPagingGalleryUseCase
import com.example.skillcinema.entity.film.Image
import com.example.skillcinema.entity.film.types.GalleryType
import com.example.skillcinema.presentation.screen.abstractTemplates.AbstractViewModelWithErrorChannel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow

class GalleryPagerViewModel @AssistedInject constructor(
    getPagingGalleryUseCase: GetPagingGalleryUseCase,
    @Assisted kinopoiskId: Int,
    @Assisted galleryType: GalleryType
) : AbstractViewModelWithErrorChannel() {

    val pagedListFlow: Flow<PagingData<Image>> = Pager(
        PagingConfig(20)
    ) {
        getPagingGalleryUseCase.execute(kinopoiskId, galleryType)
    }.flow.cachedIn(viewModelScopeWithExceptionHandler)

    @AssistedFactory
    interface Factory {
        fun create(kinopoiskId: Int, galleryType: GalleryType): GalleryPagerViewModel
    }
}