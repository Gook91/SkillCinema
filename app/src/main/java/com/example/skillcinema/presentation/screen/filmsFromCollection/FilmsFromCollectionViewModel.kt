package com.example.skillcinema.presentation.screen.filmsFromCollection

import com.example.skillcinema.domain.userCollections.GetFilmsFromCollectionByType
import com.example.skillcinema.entity.collections.TypeCollection
import com.example.skillcinema.presentation.screen.abstractTemplates.AbstractViewModelWithErrorChannel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class FilmsFromCollectionViewModel @AssistedInject constructor(
    getFilmsFromCollectionByType: GetFilmsFromCollectionByType,
    @Assisted collectionType: TypeCollection
) : AbstractViewModelWithErrorChannel() {
    val filmsFlow = getFilmsFromCollectionByType.execute(collectionType)
        .stateIn(
            viewModelScopeWithExceptionHandler,
            SharingStarted.WhileSubscribed(5_000L),
            null
        )

    @AssistedFactory
    interface Factory {
        fun create(collectionType: TypeCollection): FilmsFromCollectionViewModel
    }
}