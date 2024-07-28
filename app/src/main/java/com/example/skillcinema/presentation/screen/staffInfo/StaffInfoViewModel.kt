package com.example.skillcinema.presentation.screen.staffInfo

import com.example.skillcinema.domain.staff.GetStaffInfoUseCase
import com.example.skillcinema.domain.staff.GetFilmsFromStaffUseCase
import com.example.skillcinema.entity.film.Film
import com.example.skillcinema.entity.person.Staff
import com.example.skillcinema.presentation.screen.abstractTemplates.AbstractViewModelWithErrorChannel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class StaffInfoViewModel @AssistedInject constructor(
    getStaffInfoUseCase: GetStaffInfoUseCase,
    getFilmsFromStaffUseCase: GetFilmsFromStaffUseCase,
    @Assisted staffId: Int,
) : AbstractViewModelWithErrorChannel() {

    val staffFlow = flow<Staff?> {
        val staff = getStaffInfoUseCase.execute(staffId)
        emit(staff)
    }.stateIn(
        viewModelScopeWithExceptionHandler,
        SharingStarted.WhileSubscribed(5_000L),
        null
    )

    val filmsFromStaffFlow = flow<List<Film>?> {
        val filmList = getFilmsFromStaffUseCase.execute(staffId)
        emit(filmList)
    }.stateIn(
        viewModelScopeWithExceptionHandler,
        SharingStarted.WhileSubscribed(5_000L),
        null
    )

    @AssistedFactory
    interface Factory {
        fun create(staffId: Int): StaffInfoViewModel
    }
}