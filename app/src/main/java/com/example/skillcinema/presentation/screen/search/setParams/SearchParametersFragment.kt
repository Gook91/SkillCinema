package com.example.skillcinema.presentation.screen.search.setParams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.skillcinema.App
import com.example.skillcinema.R
import com.example.skillcinema.databinding.FragmentSearchParametersBinding
import com.example.skillcinema.entity.film.types.TypeFilm
import com.example.skillcinema.entity.query.TypeSort
import com.example.skillcinema.presentation.screen.abstractTemplates.AbstractFragmentWithErrorMessageAndInsets
import com.example.skillcinema.presentation.screen.search.SearchViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SearchParametersFragment : AbstractFragmentWithErrorMessageAndInsets() {

    override val viewModel: SearchViewModel by activityViewModels {
        (requireContext().applicationContext as App).appComponent.searchViewModelFactory()
    }

    private var _binding: FragmentSearchParametersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchParametersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            when (viewModel.selectedTypeFilmFlow.value) {
                TypeFilm.FILM -> binding.showOnlyFilms.isChecked = true
                TypeFilm.TV_SERIES -> binding.showOnlySerials.isChecked = true
                else -> binding.showAllFilms.isChecked = true
            }
            binding.selectShowFilms.addOnButtonCheckedListener { _, checkedId, isChecked ->
                if (!isChecked) return@addOnButtonCheckedListener
                when (checkedId) {
                    binding.showOnlyFilms.id -> viewModel.setTypeFilm(TypeFilm.FILM)
                    binding.showOnlySerials.id -> viewModel.setTypeFilm(TypeFilm.TV_SERIES)
                    else -> viewModel.setTypeFilm(null)
                }
            }
        }

        binding.selectCountry.setOnClickListener {
            findNavController().navigate(R.id.action_search_parameters_fragment_to_set_country_fragment)
        }
        viewModel.selectedCountryFlow.onEach { country ->
            binding.selectedCountry.text = country ?: getString(R.string.not_selected)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.selectGenre.setOnClickListener {
            findNavController().navigate(R.id.action_search_parameters_fragment_to_set_genre_fragment)
        }
        viewModel.selectedGenreFlow.onEach { genre ->
            binding.selectedGenre.text = genre ?: getString(R.string.not_selected)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.selectYears.setOnClickListener {
            findNavController().navigate(R.id.action_search_parameters_fragment_to_set_years_fragment)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            combine(
                viewModel.selectedYearFromFlow,
                viewModel.selectedYearToFlow
            ) { yearFrom, yearTo ->
                if (yearFrom == null && yearTo == null) return@combine getString(R.string.any)
                val yearFromString = yearFrom?.let { getString(R.string.year_from, it) } ?: ""
                val yearToString = yearTo?.let { getString(R.string.year_to, it) } ?: ""
                return@combine yearFromString + yearToString
            }.stateIn(viewLifecycleOwner.lifecycleScope).collect {
                binding.selectedYears.text = it
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            val (vmRatingFrom, vmRatingTo) = viewModel.selectedRatingFlow.value
            binding.ratingSlider.values = listOf(vmRatingFrom.toFloat(), vmRatingTo.toFloat())
            updateRatingNumbers(vmRatingFrom, vmRatingTo)

            binding.ratingSlider.addOnChangeListener { slider, _, _ ->
                val ratingFrom = slider.values[0].toInt()
                val ratingTo = slider.values[1].toInt()

                updateRatingNumbers(ratingFrom, ratingTo)

                viewModel.setRating(ratingFrom, ratingTo)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            when (viewModel.selectedSortTypeFlow.value) {
                TypeSort.YEAR -> binding.sortByDate.isChecked = true
                TypeSort.NUM_VOTE -> binding.sortByPopularity.isChecked = true
                TypeSort.RATING -> binding.sortByRating.isChecked = true
            }
            binding.selectSort.addOnButtonCheckedListener { _, checkedId, isChecked ->
                if (!isChecked) return@addOnButtonCheckedListener
                when (checkedId) {
                    binding.sortByDate.id -> viewModel.setSortType(TypeSort.YEAR)
                    binding.sortByPopularity.id -> viewModel.setSortType(TypeSort.NUM_VOTE)
                    binding.sortByRating.id -> viewModel.setSortType(TypeSort.RATING)
                }
            }
        }

        binding.isShowNotViewed.addOnCheckedChangeListener { button, isChecked ->
            viewModel.setIsShowOnlyNotViewed(isChecked)
            if (isChecked) button.setIconTintResource(R.color.blue)
            else button.setIconTintResource(R.color.black)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            binding.isShowNotViewed.isChecked =
                viewModel.selectedIsShowOnlyNotViewedFlow.value
        }
    }

    private fun updateRatingNumbers(ratingFrom: Int, ratingTo: Int) {
        binding.ratingFrom.text = ratingFrom.toString()
        binding.ratingTo.text = ratingTo.toString()
        binding.selectedRating.text = if (ratingFrom == 0 && ratingTo == 10)
            getString(R.string.any)
        else
            getString(R.string.rating_values, ratingFrom, ratingTo)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}