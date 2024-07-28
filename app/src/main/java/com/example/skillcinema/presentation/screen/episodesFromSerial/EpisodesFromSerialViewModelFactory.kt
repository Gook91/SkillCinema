package com.example.skillcinema.presentation.screen.episodesFromSerial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class EpisodesFromSerialViewModelFactory(
    private val kinopoiskId: Int,
    private val create: (Int) -> ViewModel
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(EpisodesFromSerialViewModel::class.java))
            return create(kinopoiskId) as T
        throw IllegalArgumentException()
    }
}