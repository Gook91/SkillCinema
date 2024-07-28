package com.example.skillcinema.presentation.screen.main

import com.example.skillcinema.domain.previews.GetPreviewFilmListUseCase
import com.example.skillcinema.domain.previews.GetPreviewPremieresUseCase
import com.example.skillcinema.domain.previews.GetPreviewTopListUseCase
import com.example.skillcinema.domain.previews.GetRandomGenreAndCountryUseCase
import com.example.skillcinema.entity.response.ResponseList
import com.example.skillcinema.entity.film.Film
import com.example.skillcinema.entity.query.QueryType
import com.example.skillcinema.presentation.screen.abstractTemplates.AbstractViewModelWithErrorChannel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel @AssistedInject constructor(
    private val getRandomGenreAndCountryUseCase: GetRandomGenreAndCountryUseCase,
    private val getPreviewFilmListUseCase: GetPreviewFilmListUseCase,
    private val getPreviewTopListUseCase: GetPreviewTopListUseCase,
    private val getPreviewPremieresUseCase: GetPreviewPremieresUseCase,
    @Assisted queryList: List<QueryType?>
) : AbstractViewModelWithErrorChannel() {

    private val _mainScreenListsFlow =
        MutableSharedFlow<Pair<Int, ResponseList<Film>>>(replay = queryList.size)
    val mainScreenListsFlow = _mainScreenListsFlow.asSharedFlow()

    private val _randomGenresAndCountriesInQuery =
        MutableSharedFlow<Triple<Int, String, String>>(replay = queryList.size)
    val randomGenresAndCountriesInQuery = _randomGenresAndCountriesInQuery.asSharedFlow()

    init {
        queryList.forEachIndexed { i, query ->
            getFilmsAndSetInFlow(i, query)
        }
    }

    private fun getFilmsAndSetInFlow(position: Int, query: QueryType?) {
        viewModelScopeWithExceptionHandler.launch {
            val filmList = when (query) {
                is QueryType.Top -> getPreviewTopListUseCase.execute(query.type)
                is QueryType.ByParams -> getPreviewFilmListUseCase.execute(
                    query.country, query.genre, query.typeFilm
                )

                is QueryType.Premieres -> getPreviewPremieresUseCase.execute()
                null -> getRandomList(position)
                else -> return@launch
            }
            _mainScreenListsFlow.emit(position to filmList)
        }
    }

    private suspend fun getRandomList(position: Int): ResponseList<Film> {
        while (true) {
            val (genre, country) = getRandomGenreAndCountryUseCase.execute()
            val filmList = getPreviewFilmListUseCase.execute(
                country = country,
                genre = genre
            )
            if (filmList.items.isNotEmpty()) {
                _randomGenresAndCountriesInQuery.emit(Triple(position, genre, country))
                return filmList
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(queryList: List<QueryType?>): MainViewModel
    }
}