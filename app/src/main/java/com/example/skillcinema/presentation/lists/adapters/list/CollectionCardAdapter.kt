package com.example.skillcinema.presentation.lists.adapters.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.skillcinema.databinding.ItemCollectionCardBinding
import com.example.skillcinema.entity.collections.CollectionFilms
import com.example.skillcinema.entity.collections.TypeCollection
import com.example.skillcinema.presentation.lists.diffUtil.CollectionDiffUtil
import com.example.skillcinema.presentation.lists.holders.CollectionCardHolder

class CollectionCardAdapter(
    private val onClickItem: (TypeCollection) -> Unit,
    private val onDeleteItem: (Int) -> Unit
) : ListAdapter<CollectionFilms, CollectionCardHolder>(CollectionDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionCardHolder {
        val binding = ItemCollectionCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CollectionCardHolder(binding)
    }

    override fun onBindViewHolder(holder: CollectionCardHolder, position: Int) {
        val collectionInfo: CollectionFilms = getItem(position)
        holder.onBind(collectionInfo, onClickItem, onDeleteItem)
    }
}