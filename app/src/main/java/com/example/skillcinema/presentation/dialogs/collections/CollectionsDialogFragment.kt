package com.example.skillcinema.presentation.dialogs.collections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.skillcinema.App
import com.example.skillcinema.databinding.DialogCollectionsBinding
import com.example.skillcinema.entity.film.Film
import com.example.skillcinema.presentation.dialogs.AddNewCollectionDialog
import com.example.skillcinema.presentation.lists.adapters.list.CollectionListAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class CollectionsDialogFragment : BottomSheetDialogFragment() {
    private var _binding: DialogCollectionsBinding? = null
    private val binding get() = _binding!!

    private val kinopoiskId: Int by lazy {
        arguments?.getInt(KINOPOISK_ID_BUNDLE_TAG) ?: 0
    }

    private inline fun <reified VM : ViewModel> Fragment.lazyViewModel(
        noinline create: (Int) -> VM
    ) = viewModels<VM> {
        CollectionsDialogViewModelFactory(kinopoiskId, create)
    }

    private val viewModel: CollectionsDialogViewModel by lazyViewModel { lazyKinopoiskId ->
        (requireContext().applicationContext as App).appComponent.collectionsDialogViewModelFactory()
            .create(lazyKinopoiskId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogCollectionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.closeButton.setOnClickListener {
            dismiss()
        }
        binding.addNewCollection.setOnClickListener {
            childFragmentManager.setFragmentResultListener(
                AddNewCollectionDialog.GET_NEW_COLLECTION_NAME_TAG,
                viewLifecycleOwner
            ) { _, bundle ->
                val name = bundle.getString(AddNewCollectionDialog.RETURN_NAME_COLLECTION_TAG)
                name?.let { viewModel.addCollection(name) }
            }
            AddNewCollectionDialog().show(childFragmentManager, "New_collection_dialog")
        }

        val collectionAdapter = CollectionListAdapter { type, isInCollection ->
            viewModel.updateFilmInCollection(type, isInCollection)
        }
        binding.collectionList.adapter = collectionAdapter
        viewModel.collectionsFlow.filterNotNull().onEach { collections ->
            collectionAdapter.submitList(collections)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.filmInfoFlow.filterNotNull().onEach { film ->
            setFilmInfo(film)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setFilmInfo(film: Film) {
        with(binding.filmInfo) {
            filmName.text = film.nameRu ?: film.nameEn ?: film.nameOriginal

            val yearAndGenre = mutableListOf<String>()
            film.year?.let { yearAndGenre.add(it.toString()) }
            film.genres?.let { yearAndGenre.addAll(it.map { genre -> genre.genre }) }
            yearGenre.text = yearAndGenre.joinToString(SEPARATOR)

            if (film.ratingKinopoisk == null)
                rating.visibility = View.GONE
            else {
                rating.visibility = View.VISIBLE
                rating.text = film.ratingKinopoisk.toString()
            }

            Glide.with(requireContext())
                .load(film.posterUrlPreview)
                .into(poster)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val KINOPOISK_ID_BUNDLE_TAG = "kinopoisk_id"
        private const val SEPARATOR = ", "
    }
}