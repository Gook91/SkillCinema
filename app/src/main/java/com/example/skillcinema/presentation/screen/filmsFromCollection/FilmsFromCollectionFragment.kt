package com.example.skillcinema.presentation.screen.filmsFromCollection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.skillcinema.App
import com.example.skillcinema.R
import com.example.skillcinema.databinding.FragmentFilmListBinding
import com.example.skillcinema.entity.collections.TypeCollection
import com.example.skillcinema.presentation.MainActivity
import com.example.skillcinema.presentation.lists.adapters.simple.FilmCardAdapter
import com.example.skillcinema.presentation.screen.abstractTemplates.AbstractFragmentWithErrorMessageAndInsets
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class FilmsFromCollectionFragment : AbstractFragmentWithErrorMessageAndInsets() {

    private var _binding: FragmentFilmListBinding? = null
    private val binding get() = _binding!!

    private val args: FilmsFromCollectionFragmentArgs by navArgs()
    private val typeCollection: TypeCollection by lazy { args.collectionType }

    private inline fun <reified VM : ViewModel> Fragment.lazyViewModel(
        noinline create: (TypeCollection) -> VM
    ) = viewModels<VM> {
        FilmsFromCollectionViewModelFactory(typeCollection, create)
    }

    override val viewModel: FilmsFromCollectionViewModel by lazyViewModel { lazyTypeCollection ->
        (requireContext().applicationContext as App).appComponent.filmsFromCollectionViewModelFactory()
            .create(lazyTypeCollection)
    }

    override

    fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val titleName = when (typeCollection) {
            TypeCollection.Interested -> getString(R.string.interested)
            TypeCollection.Liked -> getString(R.string.liked_collection)
            TypeCollection.Viewed -> getString(R.string.viewed)
            TypeCollection.WatchLater -> getString(R.string.watch_later)
            is TypeCollection.UserCollection -> (typeCollection as TypeCollection.UserCollection).name
        }
        (activity as MainActivity).setAppBarTitle(titleName)

        val adapter = FilmCardAdapter { kinopoiskId ->
            val action =
                FilmsFromCollectionFragmentDirections.actionFilmListFragmentToFilmInfoFragment(
                    kinopoiskId
                )
            findNavController().navigate(action)
        }
        binding.dataList.adapter = adapter

        viewModel.filmsFlow.filterNotNull().onEach { filmList ->
            adapter.submitList(filmList)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}