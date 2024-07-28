package com.example.skillcinema.presentation.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.skillcinema.R
import com.example.skillcinema.databinding.DialogGalleryBinding
import com.example.skillcinema.presentation.lists.adapters.paging.PagingGalleryFullscreenAdapter
import com.example.skillcinema.presentation.lists.adapters.simple.FooterPagingLoadingAdapter
import com.example.skillcinema.presentation.screen.galleryFromFilm.galleryPagerFragment.GalleryPagerViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class GalleryDialogFragment : DialogFragment() {

    private var _binding: DialogGalleryBinding? = null
    private val binding get() = _binding!!

    override fun getTheme(): Int = R.style.FullscreenDialogTheme

    private val viewModel: GalleryPagerViewModel by viewModels({ requireParentFragment() })

    private val startImagePosition: Int by lazy {
        arguments?.getInt(POSITION_OF_OPEN_IMAGE) ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = PagingGalleryFullscreenAdapter()
        binding.galleryPager.adapter = adapter.withLoadStateFooter(FooterPagingLoadingAdapter())

        viewModel.pagedListFlow.filterNotNull().onEach { pagingGallery ->
            adapter.submitData(pagingGallery)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.galleryPager.currentItem = startImagePosition
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val POSITION_OF_OPEN_IMAGE = "position_of_open_image"
    }
}