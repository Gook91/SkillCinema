package com.example.skillcinema.presentation.lists.adapters.simple

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.example.skillcinema.databinding.ItemGalleryBinding
import com.example.skillcinema.entity.film.Image
import com.example.skillcinema.presentation.lists.diffUtil.GalleryDiffUtil
import com.example.skillcinema.presentation.lists.holders.GalleryHolder

// Адаптер для горизонтального списка прокрутки с изображениями галереи
class GalleryListAdapter : ListAdapter<Image, GalleryHolder>(GalleryDiffUtil()) {

    // Создаём элемент списка
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryHolder {
        val binding = ItemGalleryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return GalleryHolder(binding)
    }

    // Заполняем элемент списка
    override fun onBindViewHolder(holder: GalleryHolder, position: Int) {
        // Получаем ссылку и загружаем по ней картинку во вью
        val url = getItem(position).previewUrl
        Glide.with(holder.itemView.context)
            .load(url)
            .into(holder.binding.image)
    }
}