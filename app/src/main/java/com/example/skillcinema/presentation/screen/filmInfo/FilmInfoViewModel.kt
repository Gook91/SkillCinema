package com.example.skillcinema.presentation.screen.filmInfo

import com.example.skillcinema.domain.filmInfo.GetFilmInfoUseCase
import com.example.skillcinema.domain.filmInfo.GetFilmStateFlowUseCase
import com.example.skillcinema.domain.filmInfo.GetSeasonsAndSeriesUseCase
import com.example.skillcinema.domain.filmInfo.SetStateFilmUseCase
import com.example.skillcinema.domain.staff.GetStaffFromFilmUseCase
import com.example.skillcinema.domain.previews.GetPreviewGalleryUseCase
import com.example.skillcinema.domain.previews.GetPreviewSimilar
import com.example.skillcinema.entity.response.ResponseList
import com.example.skillcinema.entity.film.Film
import com.example.skillcinema.entity.film.Image
import com.example.skillcinema.entity.person.Staff
import com.example.skillcinema.entity.film.types.TypeFilm
import com.example.skillcinema.presentation.screen.abstractTemplates.AbstractViewModelWithErrorChannel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FilmInfoViewModel @AssistedInject constructor(
    private val getFilmInfoUseCase: GetFilmInfoUseCase,
    getFilmStateFlowUseCase: GetFilmStateFlowUseCase,
    private val getStaffFromFilmUseCase: GetStaffFromFilmUseCase,
    private val getPreviewSimilar: GetPreviewSimilar,
    private val getPreviewGalleryUseCase: GetPreviewGalleryUseCase,
    private val getSeasonsAndSeriesUseCase: GetSeasonsAndSeriesUseCase,
    private val setStateFilmUseCase: SetStateFilmUseCase,
    @Assisted private val kinopoiskId: Int,
) : AbstractViewModelWithErrorChannel() {

    val filmInfoFlow = flow<Film?> {
        val film = getFilmInfoUseCase.execute(kinopoiskId)
        if (film.type in typeFilmsWithSeasons) {
            val seasonsList = getSeasonsAndSeriesUseCase.execute(kinopoiskId)
            val seasonsCount = seasonsList.size
            val seriesCount = seasonsList.sumOf { season -> season.episodes.size }
            _seasonsAndSeriesFlow.value = seasonsCount to seriesCount
        }
        emit(film)
    }.stateIn(
        viewModelScopeWithExceptionHandler,
        SharingStarted.WhileSubscribed(5000L),
        null
    )

    val filmStateFlow = getFilmStateFlowUseCase.execute(kinopoiskId).stateIn(
        viewModelScopeWithExceptionHandler,
        SharingStarted.WhileSubscribed(5_000L),
        null
    )

    private val _seasonsAndSeriesFlow = MutableStateFlow<Pair<Int, Int>?>(null)
    val seasonsAndSeriesFlow = _seasonsAndSeriesFlow.asStateFlow()

    val actorsFromFilmFlow = flow<List<Staff>?> {
        val actorsFromFilm = getStaffFromFilmUseCase.execute(kinopoiskId, isActors = true)
        emit(actorsFromFilm)
    }.stateIn(
        viewModelScopeWithExceptionHandler,
        SharingStarted.WhileSubscribed(5000L),
        null
    )

    val personsFromFilmFlow = flow<List<Staff>?> {
        val personsFromFilm = getStaffFromFilmUseCase.execute(kinopoiskId, isActors = false)
        emit(personsFromFilm)
    }.stateIn(
        viewModelScopeWithExceptionHandler,
        SharingStarted.WhileSubscribed(5000L),
        null
    )

    val galleryFlow = flow<ResponseList<Image>?> {
        val galleryResponse = getPreviewGalleryUseCase.execute(kinopoiskId)
        emit(galleryResponse)
    }.stateIn(
        viewModelScopeWithExceptionHandler,
        SharingStarted.WhileSubscribed(5000L),
        null
    )

    val similarFilmsFlow = flow<ResponseList<Film>?> {
        val similarFilmList = getPreviewSimilar.execute(kinopoiskId)
        emit(similarFilmList)
    }.stateIn(
        viewModelScopeWithExceptionHandler,
        SharingStarted.WhileSubscribed(5000L),
        null
    )

    fun updateFilmState(
        isLiked: Boolean? = null,
        isWatchLater: Boolean? = null,
        isViewed: Boolean? = null
    ) {
        viewModelScopeWithExceptionHandler.launch {
            setStateFilmUseCase.execute(kinopoiskId, isLiked, isWatchLater, isViewed)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(kinopoiskId: Int): FilmInfoViewModel
    }

    companion object {
        val typeFilmsWithSeasons = listOf(
            TypeFilm.TV_SERIES.name,
            TypeFilm.MINI_SERIES.name,
            TypeFilm.TV_SHOW.name
        )
    }
}
