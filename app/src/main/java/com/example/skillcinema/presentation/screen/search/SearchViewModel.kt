package com.example.skillcinema.presentation.screen.search

import androidx.paging.cachedIn
import com.example.skillcinema.domain.paging.GetPagingFlowWithFilmsBySearchUseCase
import com.example.skillcinema.domain.search.GetCountriesUseCase
import com.example.skillcinema.domain.search.GetGenresUseCase
import com.example.skillcinema.entity.film.types.TypeFilm
import com.example.skillcinema.entity.query.QueryType
import com.example.skillcinema.entity.query.TypeSort
import com.example.skillcinema.presentation.screen.abstractTemplates.AbstractViewModelWithErrorChannel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val getPagingFlowWithFilmsBySearchUseCase: GetPagingFlowWithFilmsBySearchUseCase,
    getCountriesUseCase: GetCountriesUseCase,
    getGenresUseCase: GetGenresUseCase,
) : AbstractViewModelWithErrorChannel() {

    private val searchQueryFlow = MutableSharedFlow<QueryType.ByParams>()

    @OptIn(ExperimentalCoroutinesApi::class)
    val foundFilmsFlow = searchQueryFlow.flatMapLatest { query ->
        getPagingFlowWithFilmsBySearchUseCase.execute(query)
    }.cachedIn(viewModelScopeWithExceptionHandler)

    fun startSearch(searchText: String) {
        viewModelScopeWithExceptionHandler.launch {
            val query = QueryType.ByParams(
                title = "",
                country = selectedCountryFlow.value,
                genre = selectedGenreFlow.value,
                order = selectedSortTypeFlow.value,
                typeFilm = selectedTypeFilmFlow.value,
                ratingFrom = selectedRatingFlow.value.first,
                ratingTo = selectedRatingFlow.value.second,
                yearFrom = selectedYearFromFlow.value,
                yearTo = selectedYearToFlow.value,
                keyword = searchText,
                isShowNotViewedFilms = selectedIsShowOnlyNotViewedFlow.value
            )
            searchQueryFlow.emit(query)
        }
    }

    // Выбор типа фильма для отображения

    private val _selectedTypeFilmFlow = MutableStateFlow<TypeFilm?>(null)
    val selectedTypeFilmFlow = _selectedTypeFilmFlow.asStateFlow()

    fun setTypeFilm(typeFilm: TypeFilm?) {
        _selectedTypeFilmFlow.value = typeFilm
    }

    // Выбор страны

    val countriesFlow = flow {
        emit(getCountriesUseCase.execute())
    }.stateIn(
        viewModelScopeWithExceptionHandler,
        SharingStarted.WhileSubscribed(5_000L),
        emptyList()
    )

    private val _selectedCountryFlow = MutableStateFlow<String?>(null)
    val selectedCountryFlow = _selectedCountryFlow.asStateFlow()

    fun setCountry(country: String?) {
        _selectedCountryFlow.value = country
    }

    // Выбор жанра
    val genresFlow = flow {
        emit(getGenresUseCase.execute())
    }.stateIn(
        viewModelScopeWithExceptionHandler,
        SharingStarted.WhileSubscribed(5_000L),
        emptyList()
    )

    private val _selectedGenreFlow = MutableStateFlow<String?>(null)
    val selectedGenreFlow = _selectedGenreFlow.asStateFlow()


    fun setGenre(genre: String?) {
        _selectedGenreFlow.value = genre
    }

    // Выбор годов

    private val _selectedYearFromFlow = MutableStateFlow<Int?>(null)
    val selectedYearFromFlow = _selectedYearFromFlow.asStateFlow()

    fun setYearFrom(yearFrom: Int?) {
        _selectedYearFromFlow.value = yearFrom
    }

    private val _selectedYearToFlow = MutableStateFlow<Int?>(null)
    val selectedYearToFlow = _selectedYearToFlow.asStateFlow()

    fun setYearTo(yearFrom: Int?) {
        _selectedYearToFlow.value = yearFrom
    }


    // Выбор рейтинга

    private val _selectedRatingFlow = MutableStateFlow(DEFAULT_RATING_VALUE)
    val selectedRatingFlow = _selectedRatingFlow.asStateFlow()

    fun setRating(ratingFrom: Int, ratingTo: Int) {
        _selectedRatingFlow.value = ratingFrom to ratingTo
    }

    // Выбор сортировки

    private val _selectedSortTypeFlow = MutableStateFlow(TypeSort.RATING)
    val selectedSortTypeFlow = _selectedSortTypeFlow.asStateFlow()

    fun setSortType(typeSort: TypeSort) {
        _selectedSortTypeFlow.value = typeSort
    }

    // Выбор непросмотренных или всех фильмов

    private val _selectedIsShowOnlyNotViewedFlow = MutableStateFlow(false)
    val selectedIsShowOnlyNotViewedFlow = _selectedIsShowOnlyNotViewedFlow.asStateFlow()

    fun setIsShowOnlyNotViewed(isShow: Boolean) {
        _selectedIsShowOnlyNotViewedFlow.value = isShow
    }

    companion object {
        private val DEFAULT_RATING_VALUE = 0 to 10
    }
}