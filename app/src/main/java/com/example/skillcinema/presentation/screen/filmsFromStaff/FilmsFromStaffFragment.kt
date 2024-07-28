package com.example.skillcinema.presentation.screen.filmsFromStaff

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
import com.example.skillcinema.R
import com.example.skillcinema.databinding.FragmentFilmsFromStaffBinding
import com.example.skillcinema.presentation.customViews.TabViewWithCountItems
import com.example.skillcinema.presentation.screen.abstractTemplates.AbstractFragmentWithErrorMessageAndInsets
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class FilmsFromStaffFragment : AbstractFragmentWithErrorMessageAndInsets() {

    private var _binding: FragmentFilmsFromStaffBinding? = null
    private val binding get() = _binding!!

    private val args: FilmsFromStaffFragmentArgs by navArgs()
    private val staffId: Int by lazy { args.staffId }

    private inline fun <reified VM : ViewModel> Fragment.lazyViewModel(
        noinline create: (Int) -> ViewModel
    ) = viewModels<VM> {
        FilmsFromStaffViewModelFactory(staffId, create)
    }

    override val viewModel: FilmsFromStaffViewModel by lazyViewModel { lazyStaffId ->
        (requireContext().applicationContext as App).appComponent.filmsFromStaffViewModelFactory()
            .create(lazyStaffId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmsFromStaffBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.staffNameFlow.onEach { staffName ->
            binding.staffName.text = staffName ?: ""
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.filmsFlow.filterNotNull().onEach { filmsByProfession ->
            val viewPagerAdapter =
                FilmsFromStaffViewPagerAdapter(filmsByProfession.values.toList()) { kinopoiskId ->
                    val action = FilmsFromStaffFragmentDirections
                        .actionFilmsFromStaffFragmentToFilmInfoFragment(kinopoiskId)
                    findNavController().navigate(action)
                }
            binding.viewPager.adapter = viewPagerAdapter
            TabLayoutMediator(binding.professionTab, binding.viewPager) { tab, position ->
                val rawTabName = filmsByProfession.keys.elementAtOrNull(position)
                val tabName = when (rawTabName) {
                    "WRITER" -> getString(R.string.profession_writer)
                    "OPERATOR" -> getString(R.string.profession_operator)
                    "EDITOR" -> getString(R.string.profession_editor)
                    "COMPOSER" -> getString(R.string.profession_composer)
                    "PRODUCER_USSR" -> getString(R.string.profession_producer_ussr)
                    "HIMSELF" -> getString(R.string.profession_himself)
                    "HERSELF" -> getString(R.string.profession_herself)
                    "HRONO_TITR_MALE" -> getString(R.string.profession_hrono_titr_male)
                    "HRONO_TITR_FEMALE" -> getString(R.string.profession_hrono_titr_female)
                    "TRANSLATOR" -> getString(R.string.profession_translator)
                    "DIRECTOR" -> getString(R.string.profession_director)
                    "DESIGN" -> getString(R.string.profession_design)
                    "PRODUCER" -> getString(R.string.profession_producer)
                    "ACTOR" -> getString(R.string.profession_actor)
                    "VOICE_DIRECTOR" -> getString(R.string.profession_voice_director)
                    else -> getString(R.string.profession_unknown)
                }
                val tabCount = filmsByProfession[rawTabName]?.size ?: 0
                val tabView = TabViewWithCountItems(requireContext())
                tabView.setNameAndCount(tabName, tabCount)
                tab.customView = tabView
            }.attach()
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}