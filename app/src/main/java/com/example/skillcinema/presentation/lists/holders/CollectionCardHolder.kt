package com.example.skillcinema.presentation.lists.holders

import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.skillcinema.R
import com.example.skillcinema.databinding.ItemCollectionCardBinding
import com.example.skillcinema.entity.collections.CollectionFilms
import com.example.skillcinema.entity.collections.TypeCollection

class CollectionCardHolder(
    private val binding: ItemCollectionCardBinding
) : ViewHolder(binding.root) {

    fun onBind(
        collectionFilms: CollectionFilms,
        onClick: (TypeCollection) -> Unit,
        onClickDelete: (Int) -> Unit
    ) {
        with(binding) {
            val iconDrawable = when (collectionFilms.type) {
                TypeCollection.Liked -> R.drawable.button_liked_true
                TypeCollection.WatchLater -> R.drawable.button_watch_later
                else -> R.drawable.app_nav_profile
            }
            icon.setImageDrawable(AppCompatResources.getDrawable(itemView.context, iconDrawable))

            name.text = when (collectionFilms.type) {
                TypeCollection.Liked -> itemView.context.getString(R.string.liked_collection)
                TypeCollection.WatchLater -> itemView.context.getString(R.string.watch_later)
                TypeCollection.Interested -> itemView.context.getString(R.string.interested)
                TypeCollection.Viewed -> itemView.context.getString(R.string.viewed)
                is TypeCollection.UserCollection -> collectionFilms.type.name
            }
            count.text = collectionFilms.countFilms.toString()

            deleteButton.isVisible = collectionFilms.type is TypeCollection.UserCollection
            if (collectionFilms.type is TypeCollection.UserCollection)
                deleteButton.setOnClickListener { onClickDelete(collectionFilms.type.id) }
            else
                deleteButton.setOnClickListener {}

            root.setOnClickListener { onClick(collectionFilms.type) }
        }
    }
}