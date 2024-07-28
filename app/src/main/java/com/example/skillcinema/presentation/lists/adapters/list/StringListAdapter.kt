package com.example.skillcinema.presentation.lists.adapters.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.skillcinema.databinding.ItemStringBinding
import com.example.skillcinema.presentation.lists.diffUtil.StringDiffUtil
import com.example.skillcinema.presentation.lists.holders.StringHolder

class StringListAdapter(
    private val onClickItem: (String?) -> Unit
) : ListAdapter<String, StringHolder>(StringDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StringHolder {
        val binding = ItemStringBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StringHolder(binding)
    }

    var selectedString: String? = null

    override fun onBindViewHolder(holder: StringHolder, position: Int) {
        val itemString = getItem(position)
        val isSelected = selectedString == itemString
        holder.onBind(itemString, onClickItem, isSelected)
    }
}