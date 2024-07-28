package com.example.skillcinema.presentation.screen.filmList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skillcinema.entity.query.QueryType

class FilmListViewModelFactory constructor(
    private val queryType: QueryType,
    private val create: (QueryType) -> ViewModel,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(FilmListViewModel::class.java))
            return create(queryType) as T
        throw java.lang.IllegalArgumentException()
    }
}