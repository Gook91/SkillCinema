package com.example.skillcinema.presentation.screen.galleryFromFilm.galleryPagerFragment

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.example.skillcinema.App
import com.example.skillcinema.databinding.FragmentGalleryPagerBinding
import com.example.skillcinema.entity.film.types.GalleryType
import com.example.skillcinema.presentation.dialogs.GalleryDialogFragment
import com.example.skillcinema.presentation.lists.adapters.paging.PagingGalleryAdapter
import com.example.skillcinema.presentation.lists.adapters.simple.FooterPagingLoadingAdapter
import com.example.skillcinema.presentation.screen.abstractTemplates.AbstractFragmentWithErrorMessageAndInsets
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class GalleryPagerFragment : AbstractFragmentWithErrorMessageAndInsets() {

    private var _binding: FragmentGalleryPagerBinding? = null
    private val binding get() = _binding!!

    // Фрагмент вызывается из viewPager, который передаёт эти параметры,
    // но на всякий случай поставлены notNull заглушки
    private val kinopoiskId: Int by lazy { arguments?.getInt(KINOPOISK_ID_TAG) ?: 0 }
    private val galleryType: GalleryType by lazy {
        GalleryType.valueOf(
            arguments?.getString(
                GALLERY_TYPE_TAG
            ) ?: GalleryType.POSTER.name
        )
    }

    private inline fun <reified VM : ViewModel> Fragment.lazyViewModel(
        noinline create: (Int, GalleryType) -> VM
    ) = viewModels<VM> {
        GalleryPagerViewModelFactory(kinopoiskId, galleryType, create)
    }

    override val viewModel: GalleryPagerViewModel by lazyViewModel { lazyKinopoiskId, lazyGalleryType ->
        (requireContext().applicationContext as App).appComponent.galleryPagerViewModelFactory()
            .create(lazyKinopoiskId, lazyGalleryType)
    }

    override fun updateTopPaddingFromInsets(rootView: View) = Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PagingGalleryAdapter { position ->
            val galleryDialogFragment = GalleryDialogFragment()
            val bundle = bundleOf(GalleryDialogFragment.POSITION_OF_OPEN_IMAGE to position)
            galleryDialogFragment.arguments = bundle
            galleryDialogFragment.show(childFragmentManager, GALLERY_DIALOG_SHOW_TAG)
        }

        binding.galleryList.adapter = adapter.withLoadStateFooter(FooterPagingLoadingAdapter())
        viewModel.pagedListFlow.filterNotNull().onEach { pagingGallery ->
            adapter.submitData(pagingGallery)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val KINOPOISK_ID_TAG = "kinopoisk_id"
        const val GALLERY_TYPE_TAG = "gallery_type"

        private const val GALLERY_DIALOG_SHOW_TAG = "gallery_dialog"
    }
}