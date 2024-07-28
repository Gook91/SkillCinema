package com.example.skillcinema.presentation.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException
import javax.inject.Inject

class SearchViewModelFactory @Inject constructor(
    private val searchViewModel: SearchViewModel
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(SearchViewModel::class.java))
            return searchViewModel as T
        throw IllegalArgumentException()
    }
}