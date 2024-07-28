package com.example.skillcinema.presentation.screen.staffInfo

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
import com.bumptech.glide.Glide
import com.example.skillcinema.App
import com.example.skillcinema.R
import com.example.skillcinema.databinding.FragmentStaffInfoBinding
import com.example.skillcinema.entity.film.Film
import com.example.skillcinema.entity.person.Staff
import com.example.skillcinema.entity.person.ProfessionKey
import com.example.skillcinema.presentation.screen.abstractTemplates.AbstractFragmentWithErrorMessageAndInsets
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class StaffInfoFragment : AbstractFragmentWithErrorMessageAndInsets() {

    private var _binding: FragmentStaffInfoBinding? = null
    private val binding get() = _binding!!

    private val args: StaffInfoFragmentArgs by navArgs()
    private val staffId: Int by lazy { args.staffId }

    private inline fun <reified VM : ViewModel> Fragment.lazyViewModel(
        noinline create: (Int) -> ViewModel
    ) = viewModels<VM> {
        StaffInfoViewModelFactory(staffId, create)
    }

    override val viewModel: StaffInfoViewModel by lazyViewModel { lazyStaffId ->
        (requireContext().applicationContext as App).appComponent.staffInfoViewModelFactory()
            .create(lazyStaffId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStaffInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.staffFlow.filterNotNull().onEach { staff ->
            setMainData(staff)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.filmsFromStaffFlow.filterNotNull().onEach { filmList ->
            setFilmsFromStaff(filmList)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setMainData(staff: Staff) {
        Glide.with(requireContext())
            .load(staff.posterUrl)
            .into(binding.photo)

        binding.staffName.text = staff.nameRu ?: staff.nameEn
        binding.staffProfession.text = staff.profession
    }

    private fun setFilmsFromStaff(filmList: List<Film>) {
        if (filmList.isEmpty()) {
            binding.filmographyGroup.visibility = View.GONE
        } else {
            val filmCount = filmList.size

            val onClickItem: (Int) -> Unit = { kinopoiskId ->
                val action = StaffInfoFragmentDirections.actionStaffInfoFragmentToFilmInfoFragment(
                    kinopoiskId
                )
                findNavController().navigate(action)
            }

            val onClickLinkToAll: () -> Unit = {
                val action =
                    StaffInfoFragmentDirections.actionStaffInfoFragmentToFilmsFromStaffFragment(
                        staffId
                    )
                findNavController().navigate(action)
            }

            val bestFilms: List<Film> = filmList
                // Берём все фильмы, где человек не представляет себя
                .filter { film -> film.professionKey !in professionsNotShowInBestList }
                .sortedByDescending { film -> film.ratingKinopoisk }
                .take(COUNT_FILMS_TO_SHOW)

            binding.bestFilms.setAndShowData(
                dataList = bestFilms,
                onClickItem = onClickItem,
                onClickLinkToAll = onClickLinkToAll
            )

            binding.toListLink.setOnClickListener { onClickLinkToAll() }

            binding.countFilms.text = resources.getQuantityString(
                R.plurals.films_count,
                filmCount, filmCount
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val COUNT_FILMS_TO_SHOW = 10
        private val professionsNotShowInBestList = listOf(
            ProfessionKey.HIMSELF.name,
            ProfessionKey.HERSELF.name,
            ProfessionKey.HRONO_TITR_MALE.name,
            ProfessionKey.HRONO_TITR_FEMALE.name,
        )
    }
}