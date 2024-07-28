package com.example.skillcinema.presentation.customViews.horizontalListView.filmList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.skillcinema.databinding.FooterShowAllBinding

class FooterShowAllAdapter(
    private val onClickFooter: () -> Unit
) : Adapter<FooterShowAllHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FooterShowAllHolder {
        val binding =
            FooterShowAllBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FooterShowAllHolder(binding)
    }

    override fun onBindViewHolder(holder: FooterShowAllHolder, position: Int) {
        holder.binding.root.setOnClickListener { onClickFooter() }
    }

    override fun getItemCount(): Int = 1
}