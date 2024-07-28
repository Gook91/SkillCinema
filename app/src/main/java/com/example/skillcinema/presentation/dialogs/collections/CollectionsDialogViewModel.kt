package com.example.skillcinema.presentation.dialogs.collections

import com.example.skillcinema.domain.filmInfo.GetFilmInfoUseCase
import com.example.skillcinema.domain.filmInfo.GetCollectionsByFilmUseCase
import com.example.skillcinema.domain.userCollections.AddCollectionUseCase
import com.example.skillcinema.domain.userCollections.UpdateFilmInCollectionUseCase
import com.example.skillcinema.entity.collections.TypeCollection
import com.example.skillcinema.presentation.screen.abstractTemplates.AbstractViewModelWithErrorChannel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CollectionsDialogViewModel @AssistedInject constructor(
    getCollectionsByFilmUseCase: GetCollectionsByFilmUseCase,
    getFilmInfoUseCase: GetFilmInfoUseCase,
    private val addCollectionUseCase: AddCollectionUseCase,
    private val updateFilmInCollectionUseCase: UpdateFilmInCollectionUseCase,
    @Assisted private val kinopoiskId: Int
) : AbstractViewModelWithErrorChannel() {

    val collectionsFlow = getCollectionsByFilmUseCase.execute(kinopoiskId)
        .stateIn(
            viewModelScopeWithExceptionHandler,
            SharingStarted.WhileSubscribed(5_000L),
            null
        )

    val filmInfoFlow = flow {
        val filmInfo = getFilmInfoUseCase.execute(kinopoiskId)
        emit(filmInfo)
    }.stateIn(
        viewModelScopeWithExceptionHandler,
        SharingStarted.WhileSubscribed(5_000L),
        null
    )

    fun addCollection(name: String) {
        viewModelScopeWithExceptionHandler.launch {
            addCollectionUseCase.execute(name)
        }
    }

    fun updateFilmInCollection(typeCollection: TypeCollection, isFilmInCollection: Boolean) {
        viewModelScopeWithExceptionHandler.launch {
            updateFilmInCollectionUseCase.execute(typeCollection, kinopoiskId, isFilmInCollection)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(kinopoiskId: Int): CollectionsDialogViewModel
    }
}