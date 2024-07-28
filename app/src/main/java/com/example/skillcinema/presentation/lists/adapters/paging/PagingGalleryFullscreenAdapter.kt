package com.example.skillcinema.presentation.lists.adapters.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.bumptech.glide.Glide
import com.example.skillcinema.databinding.ItemGalleryFullscreenBinding
import com.example.skillcinema.entity.film.Image
import com.example.skillcinema.presentation.lists.diffUtil.GalleryDiffUtil
import com.example.skillcinema.presentation.lists.holders.GalleryFullscreenHolder

class PagingGalleryFullscreenAdapter :
    PagingDataAdapter<Image, GalleryFullscreenHolder>(GalleryDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryFullscreenHolder {
        val binding = ItemGalleryFullscreenBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GalleryFullscreenHolder(binding)
    }

    override fun onBindViewHolder(holder: GalleryFullscreenHolder, position: Int) {
        val imageUrl = getItem(position)?.imageUrl ?: return
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .into(holder.binding.image)
    }
}