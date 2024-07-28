package com.example.skillcinema.presentation.welcomeActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillcinema.domain.settings.IsFirstLaunchUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class WelcomeViewModel @Inject constructor(
    isFirstLaunchUseCase: IsFirstLaunchUseCase
) : ViewModel() {
    val isFirstLaunchFlow = flow {
        val isFirstLaunch = isFirstLaunchUseCase.execute()
        emit(isFirstLaunch)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000L),
        true
    )
}