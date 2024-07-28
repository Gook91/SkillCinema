package com.example.skillcinema.presentation.lists.adapters.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.example.skillcinema.databinding.ItemFilmListBinding
import com.example.skillcinema.entity.film.Film
import com.example.skillcinema.presentation.lists.diffUtil.FilmDiffUtil
import com.example.skillcinema.presentation.lists.holders.FilmListHolder

class PagingListFilmAdapter(
    private val onClickItem: (Int) -> Unit
) : PagingDataAdapter<Film, FilmListHolder>(FilmDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmListHolder {
        val binding =
            ItemFilmListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmListHolder(binding)
    }

    override fun onBindViewHolder(holder: FilmListHolder, position: Int) {
        val film = getItem(position) ?: return
        holder.onBind(film, onClickItem)
    }
}