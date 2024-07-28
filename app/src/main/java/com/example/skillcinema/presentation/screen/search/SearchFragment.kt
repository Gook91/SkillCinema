package com.example.skillcinema.presentation.screen.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.skillcinema.App
import com.example.skillcinema.R
import com.example.skillcinema.databinding.FragmentSearchBinding
import com.example.skillcinema.presentation.lists.adapters.paging.PagingListFilmAdapter
import com.example.skillcinema.presentation.lists.adapters.simple.FooterPagingLoadingAdapter
import com.example.skillcinema.presentation.screen.abstractTemplates.AbstractFragmentWithErrorMessageAndInsets
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SearchFragment : AbstractFragmentWithErrorMessageAndInsets() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override val viewModel: SearchViewModel by activityViewModels {
        (requireContext().applicationContext as App).appComponent.searchViewModelFactory()
    }

    override fun getActionBarHeight(): Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchParams.setOnClickListener {
            findNavController().navigate(R.id.action_search_fragment_to_search_parameters_fragment)
        }

        setStartSearch()

        setAdapter()
    }

    private fun setStartSearch() {
        var searchDelayJob: Job? = null
        binding.searchText.doOnTextChanged { text, _, _, _ ->
            searchDelayJob?.cancel()
            binding.searchProgress.visibility = View.VISIBLE
            if (!text.isNullOrBlank()) {
                searchDelayJob = viewLifecycleOwner.lifecycleScope.launch {
                    delay(DELAY_BEFORE_SEARCH)
                    viewModel.startSearch(text.toString())
                }
            }
        }
    }

    private fun setAdapter() {
        val foundFilmsAdapter = PagingListFilmAdapter { kinopoiskId ->
            val action =
                SearchFragmentDirections.actionSearchFragmentToFilmInfoFragment(kinopoiskId)
            findNavController().navigate(action)
        }
        binding.foundFilms.adapter = foundFilmsAdapter
            .withLoadStateFooter(FooterPagingLoadingAdapter())

        viewModel.foundFilmsFlow.onEach {
            foundFilmsAdapter.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        foundFilmsAdapter.loadStateFlow.onEach { state ->
            if (state.refresh == LoadState.Loading) {
                binding.searchProgress.visibility = View.VISIBLE
                binding.nothingFound.visibility = View.GONE
            } else {
                binding.searchProgress.visibility = View.GONE
            }

            if (state.append.endOfPaginationReached && foundFilmsAdapter.itemCount < 1)
                binding.nothingFound.visibility = View.VISIBLE
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        viewModel.startSearch(binding.searchText.text.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val DELAY_BEFORE_SEARCH: Long = 1_000L
    }
}