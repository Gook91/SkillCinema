package com.example.skillcinema.presentation.lists.adapters.simple

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.skillcinema.databinding.ItemFilmCardBinding
import com.example.skillcinema.entity.film.Film
import com.example.skillcinema.presentation.lists.diffUtil.FilmDiffUtil
import com.example.skillcinema.presentation.lists.holders.FilmCardHolder

// Адаптер для горизонтального списка прокрутки с фильмами
class FilmCardAdapter(
    private val onClickItem: (kinopoiskId: Int) -> Unit // Слушатель щечка по элементу
) : ListAdapter<Film, FilmCardHolder>(FilmDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmCardHolder {
        val binding = ItemFilmCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FilmCardHolder(binding)
    }

    override fun onBindViewHolder(holder: FilmCardHolder, position: Int) {
        val film = getItem(position)
        holder.onBind(film, onClickItem)
    }
}