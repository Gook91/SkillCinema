package com.example.skillcinema.presentation.screen.staffList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class StaffListViewModelFactory(
    private val kinopoiskId: Int,
    private val isActors: Boolean,
    private val create: (Int, Boolean) -> ViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(StaffListViewModel::class.java))
            return create(kinopoiskId, isActors) as T
        throw java.lang.IllegalArgumentException()
    }
}