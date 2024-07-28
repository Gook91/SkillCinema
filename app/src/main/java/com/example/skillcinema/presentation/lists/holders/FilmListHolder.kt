package com.example.skillcinema.presentation.lists.holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.skillcinema.databinding.ItemFilmListBinding
import com.example.skillcinema.entity.film.Film

class FilmListHolder(
    private val binding: ItemFilmListBinding
) : ViewHolder(binding.root) {

    fun onBind(film: Film, onClickItem: (Int) -> Unit) {
        binding.filmName.text = film.nameRu ?: film.nameEn ?: film.nameOriginal

        val yearAndGenre = mutableListOf<String>()
        film.year?.let { yearAndGenre.add(it.toString()) }
        film.genres?.let { yearAndGenre.addAll(it.map { genre-> genre.genre }) }
        binding.yearGenre.text = yearAndGenre.joinToString(SEPARATOR)

        if (film.ratingKinopoisk == null)
            binding.rating.visibility = View.GONE
        else {
            binding.rating.visibility = View.VISIBLE
            binding.rating.text = film.ratingKinopoisk.toString()
        }

        Glide.with(itemView.context)
            .load(film.posterUrlPreview)
            .into(binding.poster)

        binding.root.setOnClickListener { onClickItem(film.kinopoiskId) }
    }

    companion object {
        private const val SEPARATOR = ", "
    }
}