package com.example.skillcinema.presentation.screen.episodesFromSerial

import com.example.skillcinema.domain.filmInfo.GetFilmNameUseCase
import com.example.skillcinema.domain.filmInfo.GetSeasonsAndSeriesUseCase
import com.example.skillcinema.entity.film.Season
import com.example.skillcinema.presentation.screen.abstractTemplates.AbstractViewModelWithErrorChannel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class EpisodesFromSerialViewModel @AssistedInject constructor(
    getFilmNameUseCase: GetFilmNameUseCase,
    getSeasonsAndSeriesUseCase: GetSeasonsAndSeriesUseCase,
    @Assisted kinopoiskId: Int
) : AbstractViewModelWithErrorChannel() {

    val episodesFlow = flow<List<Season>?> {
        val seasons = getSeasonsAndSeriesUseCase.execute(kinopoiskId)
        emit(seasons)
    }.stateIn(
        viewModelScopeWithExceptionHandler,
        SharingStarted.WhileSubscribed(5_000L),
        null
    )

    val filmNameFlow = flow {
        val filmName = getFilmNameUseCase.execute(kinopoiskId)
        emit(filmName)
    }.stateIn(
        viewModelScopeWithExceptionHandler,
        SharingStarted.WhileSubscribed(5_000L),
        ""
    )

    @AssistedFactory
    interface Factory {
        fun create(kinopoiskId: Int): EpisodesFromSerialViewModel
    }
}