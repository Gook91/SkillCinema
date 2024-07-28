package com.example.skillcinema.presentation.screen.filmsFromStaff

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FilmsFromStaffViewModelFactory(
    private val staffId: Int,
    private val create: (Int) -> ViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(FilmsFromStaffViewModel::class.java))
            return create(staffId) as T
        throw java.lang.IllegalArgumentException()
    }
}