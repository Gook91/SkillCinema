package com.example.skillcinema.presentation.lists.adapters.simple

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.skillcinema.databinding.ItemFilmListBinding
import com.example.skillcinema.entity.film.Film
import com.example.skillcinema.presentation.lists.holders.FilmListHolder

class FilmListAdapter(
    private val dataList: List<Film>,
    private val onClickItem: (kinopoiskId: Int) -> Unit
) : Adapter<FilmListHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmListHolder {
        val binding = ItemFilmListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FilmListHolder(binding)
    }

    override fun onBindViewHolder(holder: FilmListHolder, position: Int) {
        val film = dataList[position]
        holder.onBind(film, onClickItem)
    }

    override fun getItemCount(): Int = dataList.size
}