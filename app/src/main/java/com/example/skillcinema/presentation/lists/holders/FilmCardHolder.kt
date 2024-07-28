package com.example.skillcinema.presentation.lists.holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.skillcinema.R
import com.example.skillcinema.databinding.ItemFilmCardBinding
import com.example.skillcinema.entity.film.Film

class FilmCardHolder(
    private val binding: ItemFilmCardBinding
) : ViewHolder(binding.root) {

    fun onBind(film: Film, onClickItem: (Int) -> Unit) {
        with(binding) {
            // Устанавливаем значения всем вью и слушатель всему элементу
            name.text = film.nameRu ?: film.nameEn ?: film.nameOriginal

            // При выводе списка фильма по актёру - у фильма присутствует профессия или роль,
            // поэтому показываем её, иначе показываем жанр
            description.text = if (film.personDescription != null)
                film.personDescription
            else
                film.genres?.first()?.genre


            if (film.ratingKinopoisk == null)
                rating.visibility = View.GONE
            else {
                rating.visibility = View.VISIBLE
                rating.text = film.ratingKinopoisk.toString()
            }
            if (film.isViewed) {
                poster.foreground = androidx.core.content.res.ResourcesCompat.getDrawable(
                    itemView.resources,
                    R.drawable.mask_viewed,
                    null
                )
                isViewed.visibility = View.VISIBLE
            } else {
                poster.foreground = null
                isViewed.visibility = View.GONE
            }

            Glide.with(itemView.context)
                .load(film.posterUrlPreview)
                .into(poster)
            root.setOnClickListener { onClickItem(film.kinopoiskId) }
        }
    }
}