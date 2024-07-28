package com.example.skillcinema.presentation.screen.filmList

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.skillcinema.domain.paging.GetPagingFilmsUseCase
import com.example.skillcinema.entity.film.Film
import com.example.skillcinema.entity.query.QueryType
import com.example.skillcinema.presentation.screen.abstractTemplates.AbstractViewModelWithErrorChannel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow

class FilmListViewModel @AssistedInject constructor(
    getPagingFilmsUseCase: GetPagingFilmsUseCase,
    @Assisted queryType: QueryType
) : AbstractViewModelWithErrorChannel() {
    val pagedFilmsFlow: Flow<PagingData<Film>> = Pager(
        PagingConfig(20)
    ) {
        getPagingFilmsUseCase.execute(
            queryType
        )
    }.flow.cachedIn(viewModelScopeWithExceptionHandler)

    @AssistedFactory
    interface Factory{
        fun create(queryType: QueryType): FilmListViewModel
    }
}