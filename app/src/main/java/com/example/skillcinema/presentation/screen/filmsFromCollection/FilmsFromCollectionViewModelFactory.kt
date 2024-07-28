package com.example.skillcinema.presentation.screen.filmsFromCollection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skillcinema.entity.collections.TypeCollection
import java.lang.IllegalArgumentException

class FilmsFromCollectionViewModelFactory(
    private val collectionType: TypeCollection,
    private val create: (TypeCollection) -> ViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(FilmsFromCollectionViewModel::class.java))
            return create(collectionType) as T
        throw IllegalArgumentException()
    }
}