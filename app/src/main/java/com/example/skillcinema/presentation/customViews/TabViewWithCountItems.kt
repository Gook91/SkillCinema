package com.example.skillcinema.presentation.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.skillcinema.databinding.ViewCustomTabBinding

class TabViewWithCountItems @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = ViewCustomTabBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)
    }

    fun setNameAndCount(name: String, count: Int? = null) {
        binding.tabName.text = name
        if (count == null)
            binding.countItemsInTab.visibility = GONE
        else {
            binding.countItemsInTab.visibility = VISIBLE
            binding.countItemsInTab.text = count.toString()
        }
    }
}