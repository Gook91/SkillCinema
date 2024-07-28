package com.example.skillcinema.presentation.lists.holders

import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.skillcinema.R
import com.example.skillcinema.databinding.ItemCollectionListBinding
import com.example.skillcinema.entity.collections.CollectionFilms
import com.example.skillcinema.entity.collections.TypeCollection

class CollectionListHolder(
    private val binding: ItemCollectionListBinding
) : ViewHolder(binding.root) {

    fun onBind(
        collectionFilms: CollectionFilms,
        onClickItem: (TypeCollection, Boolean) -> Unit
    ) {
        with(binding) {
            collectionName.text = when (collectionFilms.type) {
                TypeCollection.Liked -> itemView.context.getString(R.string.liked_collection)
                TypeCollection.WatchLater -> itemView.context.getString(R.string.watch_later)
                TypeCollection.Interested -> itemView.context.getString(R.string.interested)
                TypeCollection.Viewed -> itemView.context.getString(R.string.viewed)
                is TypeCollection.UserCollection -> collectionFilms.type.name
            }
            collectionCountFilms.text = collectionFilms.countFilms.toString()

            isAddedToCollection.isChecked = collectionFilms.isMovieInCollection ?: false
            isAddedToCollection.setOnClickListener {
                onClickItem(collectionFilms.type, (it as CheckBox).isChecked)
            }
        }
    }
}