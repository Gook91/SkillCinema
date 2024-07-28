package com.example.skillcinema.presentation.screen.staffList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.skillcinema.App
import com.example.skillcinema.databinding.FragmentStaffListBinding
import com.example.skillcinema.presentation.MainActivity
import com.example.skillcinema.presentation.lists.adapters.simple.StaffListAdapter
import com.example.skillcinema.presentation.screen.abstractTemplates.AbstractFragmentWithErrorMessageAndInsets
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class StaffListFragment : AbstractFragmentWithErrorMessageAndInsets() {

    private var _binding: FragmentStaffListBinding? = null
    private val binding get() = _binding!!

    private val args: StaffListFragmentArgs by navArgs()
    private val kinopoiskId: Int by lazy { args.kinopoiskId }
    private val isActors: Boolean by lazy { args.isActors }

    private inline fun <reified VM : ViewModel> Fragment.lazyViewModel(
        noinline create: (Int, Boolean) -> ViewModel
    ) = viewModels<VM> {
        StaffListViewModelFactory(kinopoiskId, isActors, create)
    }

    override val viewModel: StaffListViewModel by lazyViewModel { i, b ->
        (requireContext().applicationContext as App).appComponent.staffListViewModelFactory()
            .create(i, b)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStaffListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.filmNameFlow.onEach { filmName ->
            (activity as MainActivity).setAppBarTitle(filmName)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.staffListFlow.filterNotNull().onEach { dataList ->
            binding.loadingProgress.visibility = View.GONE
            val adapter = StaffListAdapter { staffId ->
                val action =
                    StaffListFragmentDirections.actionStaffListFragmentToStaffInfoFragment(staffId)
                findNavController().navigate(action)
            }
            adapter.submitData(dataList)
            binding.dataList.adapter = adapter

        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}