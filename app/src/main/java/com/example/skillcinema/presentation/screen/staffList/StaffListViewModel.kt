package com.example.skillcinema.presentation.screen.staffList

import com.example.skillcinema.domain.filmInfo.GetFilmNameUseCase
import com.example.skillcinema.domain.staff.GetStaffFromFilmUseCase
import com.example.skillcinema.entity.person.Staff
import com.example.skillcinema.presentation.screen.abstractTemplates.AbstractViewModelWithErrorChannel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class StaffListViewModel @AssistedInject constructor(
    getFilmNameUseCase: GetFilmNameUseCase,
    getStaffFromFilmUseCase: GetStaffFromFilmUseCase,
    @Assisted kinopoiskId: Int,
    @Assisted isActors: Boolean
) : AbstractViewModelWithErrorChannel() {

    val filmNameFlow = flow{
        val filmName = getFilmNameUseCase.execute(kinopoiskId)
        emit(filmName)
    }.stateIn(
        viewModelScopeWithExceptionHandler,
        SharingStarted.WhileSubscribed(5_000L),
        ""
    )

    val staffListFlow = flow<List<Staff>?> {
        val personList = getStaffFromFilmUseCase.execute(kinopoiskId, isActors)
        emit(personList)
    }.stateIn(
        viewModelScopeWithExceptionHandler,
        SharingStarted.WhileSubscribed(5_000L),
        null
    )

    @AssistedFactory
    interface Factory {
        fun create(kinopoiskId: Int, isActors: Boolean): StaffListViewModel
    }
}