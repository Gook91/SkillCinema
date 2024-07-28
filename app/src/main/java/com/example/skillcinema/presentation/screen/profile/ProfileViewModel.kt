package com.example.skillcinema.presentation.screen.profile

import com.example.skillcinema.domain.userCollections.AddCollectionUseCase
import com.example.skillcinema.domain.userCollections.ClearInterestedHistoryUseCase
import com.example.skillcinema.domain.userCollections.ClearViewedHistoryUseCase
import com.example.skillcinema.domain.userCollections.DeleteUserCollectionUseCase
import com.example.skillcinema.domain.userCollections.GetCollectionsWithCountUseCase
import com.example.skillcinema.domain.userCollections.GetCountInterestedFilmsUseCase
import com.example.skillcinema.domain.userCollections.GetCountViewedFilmsUseCase
import com.example.skillcinema.domain.userCollections.GetFilmsFromCollectionByType
import com.example.skillcinema.entity.collections.TypeCollection
import com.example.skillcinema.presentation.screen.abstractTemplates.AbstractViewModelWithErrorChannel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    getFilmsFromCollectionByType: GetFilmsFromCollectionByType,
    getCountViewedFilmsUseCase: GetCountViewedFilmsUseCase,
    private val clearViewedHistoryUseCase: ClearViewedHistoryUseCase,
    getCollectionsWithCountUseCase: GetCollectionsWithCountUseCase,
    private val addCollectionUseCase: AddCollectionUseCase,
    private val deleteUserCollectionUseCase: DeleteUserCollectionUseCase,
    getCountInterestedFilmsUseCase: GetCountInterestedFilmsUseCase,
    private val clearInterestedHistoryUseCase: ClearInterestedHistoryUseCase
) : AbstractViewModelWithErrorChannel() {

    // Просмотренные фильмы

    val viewedFilmsFlow = getFilmsFromCollectionByType.execute(TypeCollection.Viewed)
        .map { it.take(FILMS_IN_LIST_MAX_COUNT) }
        .stateIn(
            viewModelScopeWithExceptionHandler,
            SharingStarted.WhileSubscribed(5_000L),
            null
        )

    val countViewedFilmsFlow = getCountViewedFilmsUseCase.execute()
        .stateIn(
            viewModelScopeWithExceptionHandler,
            SharingStarted.WhileSubscribed(5_000L),
            null
        )

    fun clearViewedHistory() {
        viewModelScopeWithExceptionHandler.launch {
            clearViewedHistoryUseCase.execute()
        }
    }

    // Коллекции

    val collectionsFlow = getCollectionsWithCountUseCase.execute()
        .stateIn(
            viewModelScopeWithExceptionHandler,
            SharingStarted.WhileSubscribed(5_000L),
            null
        )

    fun addCollection(name: String) {
        viewModelScopeWithExceptionHandler.launch {
            addCollectionUseCase.execute(name)
        }
    }

    fun deleteCollection(collectionId: Int) {
        viewModelScopeWithExceptionHandler.launch {
            deleteUserCollectionUseCase.execute(collectionId)
        }
    }

    // Фильмы, которыми интересовались

    val interestedFilmsFlow = getFilmsFromCollectionByType.execute(TypeCollection.Interested)
        .map { it.take(FILMS_IN_LIST_MAX_COUNT) }
        .stateIn(
            viewModelScopeWithExceptionHandler,
            SharingStarted.WhileSubscribed(5_000L),
            null
        )

    val countInterestedFilmsFlow = getCountInterestedFilmsUseCase.execute()
        .stateIn(
            viewModelScopeWithExceptionHandler,
            SharingStarted.WhileSubscribed(5_000L),
            null
        )

    fun clearInterestedHistory() {
        viewModelScopeWithExceptionHandler.launch {
            clearInterestedHistoryUseCase.execute()
        }
    }

    companion object {
        private const val FILMS_IN_LIST_MAX_COUNT = 20
    }
}