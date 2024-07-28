package com.example.skillcinema.presentation.lists.holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.skillcinema.R
import com.example.skillcinema.databinding.ItemEpisodeBinding
import com.example.skillcinema.entity.film.Episode

class EpisodeHolder(
    val binding: ItemEpisodeBinding
) : ViewHolder(binding.root) {

    fun onBind(episode: Episode) {
        with(binding) {
            numberAndName.text =
                itemView.context.getString(
                    R.string.episode_number_and_name,
                    episode.episodeNumber,
                    episode.nameRu ?: episode.nameEn ?: ""
                )

            if (episode.synopsis.isNullOrBlank())
                synopsis.visibility = View.GONE
            else
                synopsis.visibility = View.VISIBLE
            synopsis.text = episode.synopsis

            if (episode.releaseDate != null)
                releaseDate.visibility = View.VISIBLE
            else
                releaseDate.visibility = View.GONE
            releaseDate.text = episode.releaseDate
        }
    }
}