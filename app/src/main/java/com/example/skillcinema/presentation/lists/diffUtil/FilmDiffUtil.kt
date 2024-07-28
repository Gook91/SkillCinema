package com.example.skillcinema.presentation.lists.diffUtil

import androidx.recyclerview.widget.DiffUtil
import com.example.skillcinema.entity.film.Film

class FilmDiffUtil : DiffUtil.ItemCallback<Film>() {
    override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean =
        oldItem.kinopoiskId == newItem.kinopoiskId

    override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean {
        return oldItem.kinopoiskId == newItem.kinopoiskId &&
                oldItem.nameRu == newItem.nameRu &&
                oldItem.nameEn == newItem.nameEn &&
                oldItem.year == newItem.year &&
                oldItem.countries == newItem.countries &&
                oldItem.genres == newItem.genres &&
                oldItem.ratingKinopoisk == newItem.ratingKinopoisk &&
                oldItem.ratingKinopoiskVoteCount == newItem.ratingKinopoiskVoteCount &&
                oldItem.posterUrl == newItem.posterUrl &&
                oldItem.posterUrlPreview == newItem.posterUrlPreview
    }
}