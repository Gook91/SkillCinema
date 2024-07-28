package com.example.skillcinema.presentation.screen.episodesFromSerial

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.skillcinema.databinding.ItemPagerEpisodeListBinding
import com.example.skillcinema.entity.film.Season

class EpisodesFromSerialViewPagerAdapter(
    private val seasonList: List<Season>
) : Adapter<EpisodesFromSerialViewPagerHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EpisodesFromSerialViewPagerHolder {
        val binding = ItemPagerEpisodeListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return EpisodesFromSerialViewPagerHolder(binding)
    }

    override fun onBindViewHolder(holder: EpisodesFromSerialViewPagerHolder, position: Int) {
        val season = seasonList[position]
        holder.setData(season)
    }

    override fun getItemCount(): Int = seasonList.size
}