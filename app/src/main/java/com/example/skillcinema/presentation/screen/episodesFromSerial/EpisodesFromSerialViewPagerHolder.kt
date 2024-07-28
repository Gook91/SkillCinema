package com.example.skillcinema.presentation.screen.episodesFromSerial

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.skillcinema.R
import com.example.skillcinema.databinding.ItemPagerEpisodeListBinding
import com.example.skillcinema.entity.film.Season
import com.example.skillcinema.presentation.lists.adapters.simple.EpisodeListAdapter

class EpisodesFromSerialViewPagerHolder(
    private val binding: ItemPagerEpisodeListBinding
)    : ViewHolder(binding.root) {
    fun setData(season: Season){
        with(binding){
            val countEpisodes =
                itemView.context.resources.getQuantityString(
                    R.plurals.series_count,
                    season.episodes.size,
                    season.episodes.size
                )
            val numberAndCountString =
                itemView.context.getString(
                    R.string.season_number_and_count_episodes,
                    season.number,
                    countEpisodes
                )
            seasonInfo.text = numberAndCountString

            episodeList.adapter = EpisodeListAdapter(season.episodes)
        }
    }
}