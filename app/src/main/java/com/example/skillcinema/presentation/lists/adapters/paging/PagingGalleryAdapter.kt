package com.example.skillcinema.presentation.lists.adapters.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.bumptech.glide.Glide
import com.example.skillcinema.databinding.ItemGalleryDimensionlessBinding
import com.example.skillcinema.entity.film.Image
import com.example.skillcinema.presentation.lists.diffUtil.GalleryDiffUtil
import com.example.skillcinema.presentation.lists.holders.GalleryDimensionlessHolder

class PagingGalleryAdapter(
    private val onClickItem: (Int) -> Unit
) : PagingDataAdapter<Image, GalleryDimensionlessHolder>(GalleryDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryDimensionlessHolder {
        val binding = ItemGalleryDimensionlessBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return GalleryDimensionlessHolder(binding)
    }

    override fun onBindViewHolder(holder: GalleryDimensionlessHolder, position: Int) {
        val image = getItem(position)?.previewUrl ?: return
        Glide.with(holder.itemView.context)
            .load(image)
            .into(holder.binding.image)
        holder.binding.root.setOnClickListener { onClickItem(position) }
    }
}