package com.example.skillcinema.presentation.lists.adapters.simple

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.skillcinema.databinding.ItemWelcomeScreenBinding
import com.example.skillcinema.presentation.lists.holders.WelcomeHolder

class WelcomeAdapter(
    private val hintScreens: List<Pair<String, Int>>,
    private val clickOnItemWithPosition: (Int) -> Unit
) : Adapter<WelcomeHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WelcomeHolder {
        val binding = ItemWelcomeScreenBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return WelcomeHolder(binding)
    }

    override fun onBindViewHolder(holder: WelcomeHolder, position: Int) {
        val (textToShow, imageResource) = hintScreens[position]
        val onClickItem: () -> Unit = { clickOnItemWithPosition(position) }
        holder.onBind(imageResource, textToShow, onClickItem)
    }

    override fun getItemCount(): Int = COUNT_TIPS_SCREENS

    companion object {
        private const val COUNT_TIPS_SCREENS = 3
    }
}