package com.example.skillcinema.presentation.screen.episodesFromSerial

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
import com.example.skillcinema.databinding.FragmentEpisodesFromSerialBinding
import com.example.skillcinema.presentation.MainActivity
import com.example.skillcinema.presentation.customViews.TabViewWithCountItems
import com.example.skillcinema.presentation.screen.abstractTemplates.AbstractFragmentWithErrorMessageAndInsets
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class EpisodesFromSerialFragment : AbstractFragmentWithErrorMessageAndInsets() {

    private var _binding: FragmentEpisodesFromSerialBinding? = null
    private val binding get() = _binding!!

    private val args: EpisodesFromSerialFragmentArgs by navArgs()
    private val kinopoiskId: Int by lazy { args.kinopoiskId }

    private inline fun <reified VM : ViewModel> Fragment.lazyViewModel(
        noinline create: (Int) -> VM
    ) = viewModels<VM> {
        EpisodesFromSerialViewModelFactory(kinopoiskId, create)
    }

    override val viewModel: EpisodesFromSerialViewModel by lazyViewModel { lazyKinopoiskId ->
        (requireContext().applicationContext as App).appComponent.episodesFromSerialViewModelFactory()
            .create(lazyKinopoiskId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEpisodesFromSerialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.filmNameFlow.onEach { titleName ->
            (activity as MainActivity).setAppBarTitle(titleName)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.episodesFlow.filterNotNull().onEach { seasonList ->
            binding.episodesViewPager.adapter = EpisodesFromSerialViewPagerAdapter(seasonList)
            TabLayoutMediator(binding.seasonsTab, binding.episodesViewPager) { tab, position ->
                val tabName = seasonList[position].number.toString()
                val tabView = TabViewWithCountItems(requireContext())
                tabView.setNameAndCount(tabName)
                tab.customView = tabView
            }.attach()
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}