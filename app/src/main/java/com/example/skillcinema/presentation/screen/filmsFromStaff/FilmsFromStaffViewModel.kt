package com.example.skillcinema.presentation.screen.filmsFromStaff

import com.example.skillcinema.domain.staff.GetFilmsFromStaffUseCase
import com.example.skillcinema.domain.staff.GetStaffInfoUseCase
import com.example.skillcinema.entity.film.Film
import com.example.skillcinema.entity.person.ProfessionKey
import com.example.skillcinema.presentation.screen.abstractTemplates.AbstractViewModelWithErrorChannel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class FilmsFromStaffViewModel @AssistedInject constructor(
    getFilmsFromStaffUseCase: GetFilmsFromStaffUseCase,
    getStaffInfoUseCase: GetStaffInfoUseCase,
    @Assisted staffId: Int
) : AbstractViewModelWithErrorChannel() {

    val staffNameFlow = flow {
        val staff = getStaffInfoUseCase.execute(staffId)
        val name = staff.nameRu ?: staff.nameEn
        emit(name)
    }.stateIn(
        viewModelScopeWithExceptionHandler,
        SharingStarted.WhileSubscribed(5_000L),
        null
    )

    val filmsFlow = flow<Map<String, List<Film>>?> {
        val rawListOfFilms = getFilmsFromStaffUseCase.execute(staffId)
        val groupedFilmsByProfessionKey =
            rawListOfFilms.groupBy { it.professionKey ?: ProfessionKey.UNKNOWN.name }
        emit(groupedFilmsByProfessionKey)
    }.stateIn(
        viewModelScopeWithExceptionHandler,
        SharingStarted.WhileSubscribed(5_000L),
        null
    )

    @AssistedFactory
    interface Factory {
        fun create(staffId: Int): FilmsFromStaffViewModel
    }
}