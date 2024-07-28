package com.example.skillcinema.presentation.screen.galleryFromFilm

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.skillcinema.entity.film.types.GalleryType
import com.example.skillcinema.presentation.screen.galleryFromFilm.galleryPagerFragment.GalleryPagerFragment

class GalleryFromFilmViewPagerStateAdapter(
    fragment: Fragment,
    private val kinopoiskId: Int,
    private val availableGalleryTypes: List<GalleryType>
) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment {
        val galleryType = availableGalleryTypes[position]
        return GalleryPagerFragment().apply {
            arguments = bundleOf(
                GalleryPagerFragment.KINOPOISK_ID_TAG to kinopoiskId,
                GalleryPagerFragment.GALLERY_TYPE_TAG to galleryType.name
            )
        }
    }

    override fun getItemCount(): Int = availableGalleryTypes.size
}