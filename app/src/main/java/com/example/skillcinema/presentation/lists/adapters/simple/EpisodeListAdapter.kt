package com.example.skillcinema.presentation.lists.adapters.simple

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.skillcinema.databinding.ItemEpisodeBinding
import com.example.skillcinema.entity.film.Episode
import com.example.skillcinema.presentation.lists.holders.EpisodeHolder

class EpisodeListAdapter(
    private val dataList: List<Episode>
): Adapter<EpisodeHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeHolder {
        val binding = ItemEpisodeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EpisodeHolder(binding)
    }

    override fun onBindViewHolder(holder: EpisodeHolder, position: Int) {
        val episode = dataList[position]
        holder.onBind(episode)
    }

    override fun getItemCount(): Int = dataList.size
}