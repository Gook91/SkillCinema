package com.example.skillcinema.presentation.screen.filmsFromStaff

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.skillcinema.databinding.ItemPagerFilmListBinding
import com.example.skillcinema.entity.film.Film
import com.example.skillcinema.presentation.lists.adapters.simple.FilmListAdapter

class FilmsFromStaffViewPagerAdapter(
    private val filmsByProfession: List<List<Film>>,
    private val onClickItem: (Int) -> Unit
) : Adapter<FilmsFromStaffViewPagerHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FilmsFromStaffViewPagerHolder {
        val binding = ItemPagerFilmListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FilmsFromStaffViewPagerHolder(binding)
    }

    override fun onBindViewHolder(holder: FilmsFromStaffViewPagerHolder, position: Int) {
        val filmList = filmsByProfession[position]
        holder.binding.dataList.adapter = FilmListAdapter(filmList, onClickItem)
    }

    override fun getItemCount(): Int = filmsByProfession.size
}