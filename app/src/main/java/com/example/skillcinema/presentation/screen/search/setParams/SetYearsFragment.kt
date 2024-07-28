package com.example.skillcinema.presentation.screen.search.setParams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.skillcinema.App
import com.example.skillcinema.databinding.FragmentSetYearsBinding
import com.example.skillcinema.presentation.screen.abstractTemplates.AbstractFragmentWithErrorMessageAndInsets
import com.example.skillcinema.presentation.screen.search.SearchViewModel
import kotlinx.coroutines.launch

class SetYearsFragment : AbstractFragmentWithErrorMessageAndInsets() {

    override val viewModel: SearchViewModel by activityViewModels {
        (requireContext().applicationContext as App).appComponent.searchViewModelFactory()
    }

    private var _binding: FragmentSetYearsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetYearsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            binding.yearsFrom.selectedYear = viewModel.selectedYearFromFlow.value
            binding.yearsTo.selectedYear = viewModel.selectedYearToFlow.value
        }
        binding.selectYears.setOnClickListener {
            viewModel.setYearFrom(binding.yearsFrom.selectedYear)
            viewModel.setYearTo(binding.yearsTo.selectedYear)
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}