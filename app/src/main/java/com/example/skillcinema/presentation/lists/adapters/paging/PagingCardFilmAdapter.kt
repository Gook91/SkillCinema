package com.example.skillcinema.presentation.lists.adapters.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.example.skillcinema.databinding.ItemFilmCardBinding
import com.example.skillcinema.entity.film.Film
import com.example.skillcinema.presentation.lists.diffUtil.FilmDiffUtil
import com.example.skillcinema.presentation.lists.holders.FilmCardHolder

class PagingCardFilmAdapter(
    private val onClickItem: (Int) -> Unit
) : PagingDataAdapter<Film, FilmCardHolder>(FilmDiffUtil()) {
    // Создаём элемент списка
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmCardHolder {
        val binding = ItemFilmCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FilmCardHolder(binding)
    }

    // Заполняем элемент списка
    override fun onBindViewHolder(holder: FilmCardHolder, position: Int) {
        // Получаем данные из списка
        val film = getItem(position) ?: return
        holder.onBind(film, onClickItem)
    }
}