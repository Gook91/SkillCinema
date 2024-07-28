package com.example.skillcinema.presentation.lists.adapters.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.skillcinema.databinding.ItemCollectionListBinding
import com.example.skillcinema.entity.collections.CollectionFilms
import com.example.skillcinema.entity.collections.TypeCollection
import com.example.skillcinema.presentation.lists.diffUtil.CollectionDiffUtil
import com.example.skillcinema.presentation.lists.holders.CollectionListHolder

class CollectionListAdapter(
    private val onClickItem: (TypeCollection, Boolean) -> Unit
) : ListAdapter<CollectionFilms, CollectionListHolder>(CollectionDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionListHolder {
        val binding = ItemCollectionListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CollectionListHolder(binding)
    }

    override fun onBindViewHolder(holder: CollectionListHolder, position: Int) {
        val collectionFilms = getItem(position)
        holder.onBind(collectionFilms, onClickItem)
    }
}