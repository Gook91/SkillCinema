package com.example.skillcinema.presentation.lists.holders

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.skillcinema.databinding.ItemWelcomeScreenBinding

class WelcomeHolder(private val binding: ItemWelcomeScreenBinding): ViewHolder(binding.root) {

    fun onBind(imgResource: Int, text: String, onClickItem: () -> Unit){
        binding.welcomeImage.setImageResource(imgResource)
        binding.welcomeText.text = text
        binding.root.setOnClickListener { onClickItem() }
    }
}