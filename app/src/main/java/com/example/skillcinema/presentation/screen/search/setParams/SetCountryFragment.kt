package com.example.skillcinema.presentation.screen.search.setParams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.skillcinema.App
import com.example.skillcinema.R
import com.example.skillcinema.databinding.FragmentSetCountryOrGenreBinding
import com.example.skillcinema.presentation.lists.adapters.list.StringListAdapter
import com.example.skillcinema.presentation.screen.abstractTemplates.AbstractFragmentWithErrorMessageAndInsets
import com.example.skillcinema.presentation.screen.search.SearchViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SetCountryFragment : AbstractFragmentWithErrorMessageAndInsets() {

    override val viewModel: SearchViewModel by activityViewModels {
        (requireContext().applicationContext as App).appComponent.searchViewModelFactory()
    }

    private var _binding: FragmentSetCountryOrGenreBinding? = null
    private val binding get() = _binding!!

    private val listAdapter: StringListAdapter by lazy {
        StringListAdapter { country ->
            viewModel.setCountry(country)
            findNavController().popBackStack()
        }
    }
    private var countryList: List<String> = emptyList()
        set(value) {
            field = value
            submitDataInAdapter(value)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetCountryOrGenreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycler.adapter = listAdapter

        viewModel.countriesFlow.onEach { countries ->
            countryList = countries
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.selectedCountryFlow.onEach { selectedCountry ->
            listAdapter.selectedString = selectedCountry
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.searchText.hint = getString(R.string.enter_country)
        binding.searchText.doOnTextChanged { text, _, _, _ ->
            submitDataInAdapter(countryList.filter { it.contains(text ?: "", true) })
        }
    }

    private fun submitDataInAdapter(data: List<String>) = listAdapter.submitList(data)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}