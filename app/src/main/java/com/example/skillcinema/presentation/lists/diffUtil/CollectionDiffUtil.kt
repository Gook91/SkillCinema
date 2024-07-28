package com.example.skillcinema.presentation.lists.diffUtil

import androidx.recyclerview.widget.DiffUtil
import com.example.skillcinema.entity.collections.CollectionFilms
import com.example.skillcinema.entity.collections.TypeCollection

class CollectionDiffUtil : DiffUtil.ItemCallback<CollectionFilms>() {
    override fun areItemsTheSame(oldItem: CollectionFilms, newItem: CollectionFilms): Boolean {
        return if (oldItem.type is TypeCollection.UserCollection && newItem.type is TypeCollection.UserCollection)
            oldItem.type.id == newItem.type.id
        else
            oldItem.type == newItem.type
    }

    override fun areContentsTheSame(oldItem: CollectionFilms, newItem: CollectionFilms): Boolean {
        return oldItem.type == newItem.type &&
                oldItem.isMovieInCollection == newItem.isMovieInCollection &&
                oldItem.countFilms == newItem.countFilms
    }
}