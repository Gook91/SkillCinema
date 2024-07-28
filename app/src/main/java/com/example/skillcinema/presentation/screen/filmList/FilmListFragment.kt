package com.example.skillcinema.presentation.screen.filmList

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
import com.example.skillcinema.databinding.FragmentFilmListBinding
import com.example.skillcinema.entity.query.QueryType
import com.example.skillcinema.presentation.MainActivity
import com.example.skillcinema.presentation.lists.adapters.paging.PagingCardFilmAdapter
import com.example.skillcinema.presentation.lists.adapters.simple.FooterPagingLoadingAdapter
import com.example.skillcinema.presentation.screen.abstractTemplates.AbstractFragmentWithErrorMessageAndInsets
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class FilmListFragment : AbstractFragmentWithErrorMessageAndInsets() {

    private val args: FilmListFragmentArgs by navArgs()
    private val queryType: QueryType by lazy { args.queryType }

    private inline fun <reified VM : ViewModel> Fragment.lazyViewModel(
        noinline create: (QueryType) -> VM
    ) = viewModels<VM> {
        FilmListViewModelFactory(queryType, create)
    }

    override val viewModel: FilmListViewModel by lazyViewModel { lazyQueryType ->
        (requireContext().applicationContext as App).appComponent.filmListViewModelFactory()
            .create(lazyQueryType)
    }

    private var _binding: FragmentFilmListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setAppBarTitle(queryType.title)

        binding.root.requestApplyInsets()
        val filmPagingAdapter = PagingCardFilmAdapter { position ->
            val action = FilmListFragmentDirections
                .actionFilmListFragmentToFilmInfoFragment(position)
            findNavController().navigate(action)
        }
        binding.dataList.adapter = filmPagingAdapter.withLoadStateFooter(FooterPagingLoadingAdapter())

        viewModel.pagedFilmsFlow.onEach {
            filmPagingAdapter.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}