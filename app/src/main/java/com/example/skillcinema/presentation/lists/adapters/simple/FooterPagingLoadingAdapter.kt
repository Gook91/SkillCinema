package com.example.skillcinema.presentation.lists.adapters.simple

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.example.skillcinema.databinding.FooterPagingLoadingBinding
import com.example.skillcinema.presentation.lists.holders.FooterPagingLoadingHolder

class FooterPagingLoadingAdapter : LoadStateAdapter<FooterPagingLoadingHolder>() {
    override fun onBindViewHolder(holder: FooterPagingLoadingHolder, loadState: LoadState) = Unit

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): FooterPagingLoadingHolder {
        val binding =
            FooterPagingLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FooterPagingLoadingHolder(binding)
    }
}