package com.example.skillcinema.presentation.lists.diffUtil

import androidx.recyclerview.widget.DiffUtil
import com.example.skillcinema.entity.film.Image

class GalleryDiffUtil : DiffUtil.ItemCallback<Image>() {
    override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean =
        oldItem.imageUrl == newItem.imageUrl

    override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem.imageUrl == newItem.imageUrl &&
                oldItem.previewUrl == newItem.previewUrl
    }
}