package com.example.skillcinema.presentation.lists.holders

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.skillcinema.R
import com.example.skillcinema.databinding.ItemStringBinding

class StringHolder(
    private val binding: ItemStringBinding
) : ViewHolder(binding.root) {

    fun onBind(
        itemString: String,
        onClick: (String?) -> Unit,
        isSelectedItem: Boolean
    ) {
        binding.itemName.text = itemString

        if (isSelectedItem) {
            binding.root.setBackgroundResource(R.color.semi_transparent_gray)
            binding.root.setOnClickListener { onClick(null) }
        } else {
            binding.root.setBackgroundResource(R.color.white)
            binding.root.setOnClickListener { onClick(itemString) }
        }
    }
}