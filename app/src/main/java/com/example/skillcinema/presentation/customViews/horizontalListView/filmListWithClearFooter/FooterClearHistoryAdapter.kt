package com.example.skillcinema.presentation.customViews.horizontalListView.filmListWithClearFooter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.skillcinema.databinding.FooterClearHistoryBinding

class FooterClearHistoryAdapter(
    private val onClickFooter: () -> Unit // Слушатель для щелчка по футеру
) : Adapter<FooterClearHistoryHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FooterClearHistoryHolder {
        val binding = FooterClearHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FooterClearHistoryHolder(binding)
    }

    override fun onBindViewHolder(holder: FooterClearHistoryHolder, position: Int) {
        holder.binding.root.setOnClickListener { onClickFooter() }
    }

    override fun getItemCount(): Int = 1
}