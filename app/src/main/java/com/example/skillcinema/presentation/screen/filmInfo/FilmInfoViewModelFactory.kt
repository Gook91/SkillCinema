package com.example.skillcinema.presentation.screen.filmInfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class FilmInfoViewModelFactory @Inject constructor(
    private val kinopoiskId:Int,
    private val create: (Int) -> ViewModel
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(FilmInfoViewModel::class.java))
            return create(kinopoiskId) as T
        throw java.lang.IllegalArgumentException()
    }
}