package com.example.skillcinema.presentation.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skillcinema.entity.query.QueryType
import javax.inject.Inject

class MainViewModelFactory @Inject constructor(
    private val queryList: List<QueryType?>,
    private val create: (List<QueryType?>) -> ViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(MainViewModel::class.java))
            return create(queryList) as T
        throw java.lang.IllegalArgumentException()
    }
}