package com.example.skillcinema.presentation.screen.galleryFromFilm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.skillcinema.App
import com.example.skillcinema.R
import com.example.skillcinema.databinding.FragmentGalleryFromFilmBinding
import com.example.skillcinema.entity.film.types.GalleryType
import com.example.skillcinema.presentation.customViews.TabViewWithCountItems
import com.example.skillcinema.presentation.screen.abstractTemplates.AbstractFragmentWithErrorMessageAndInsets
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class GalleryFromFilmFragment : AbstractFragmentWithErrorMessageAndInsets() {

    private var _binding: FragmentGalleryFromFilmBinding? = null
    private val binding get() = _binding!!

    private val args: GalleryFromFilmFragmentArgs by navArgs()
    private val kinopoiskId: Int by lazy { args.kinopoiskId }

    private inline fun <reified VM : ViewModel> Fragment.lazyViewModel(
        noinline create: (Int) -> VM
    ) = viewModels<VM> {
        GalleryFromFilmViewModelFactory(kinopoiskId, create)
    }

    override val viewModel: GalleryFromFilmViewModel by lazyViewModel { lazyKinopoiskId ->
        (requireContext().applicationContext as App).appComponent.galleryFromFilmViewModelFactory()
            .create(lazyKinopoiskId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryFromFilmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.galleryTypesFlow.filterNotNull().onEach { typeMap ->
            val adapter =
                GalleryFromFilmViewPagerStateAdapter(this, kinopoiskId, typeMap.keys.toList())
            binding.galleryPager.adapter = adapter
            TabLayoutMediator(binding.galleryTypeTab, binding.galleryPager) { tab, position ->
                val tabName = typeMap.keys.elementAtOrNull(position)
                val tabCount = typeMap[tabName] ?: 0
                val tabNameLocale = when (tabName) {
                    GalleryType.STILL -> getString(R.string.gallery_still)
                    GalleryType.SHOOTING -> getString(R.string.gallery_shooting)
                    GalleryType.POSTER -> getString(R.string.gallery_poster)
                    GalleryType.FAN_ART -> getString(R.string.gallery_fan_art)
                    GalleryType.PROMO -> getString(R.string.gallery_promo)
                    GalleryType.CONCEPT -> getString(R.string.gallery_concept)
                    GalleryType.WALLPAPER -> getString(R.string.gallery_wallpaper)
                    GalleryType.COVER -> getString(R.string.gallery_cover)
                    GalleryType.SCREENSHOT -> getString(R.string.gallery_screenshot)
                    null -> getString(R.string.gallery_unknown)
                }
                val tabView = TabViewWithCountItems(requireContext())
                tabView.setNameAndCount(tabNameLocale, tabCount)
                tab.customView = tabView
            }.attach()
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}