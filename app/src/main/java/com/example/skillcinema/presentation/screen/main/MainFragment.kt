package com.example.skillcinema.presentation.screen.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.skillcinema.App
import com.example.skillcinema.R
import com.example.skillcinema.databinding.FragmentMainBinding
import com.example.skillcinema.entity.response.ResponseList
import com.example.skillcinema.entity.film.Film
import com.example.skillcinema.entity.response.TopList
import com.example.skillcinema.entity.film.types.TypeFilm
import com.example.skillcinema.entity.query.QueryType
import com.example.skillcinema.presentation.customViews.horizontalListView.filmList.HorizontalFilmListView
import com.example.skillcinema.presentation.screen.abstractTemplates.AbstractFragmentWithErrorMessageAndInsets
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take

class MainFragment : AbstractFragmentWithErrorMessageAndInsets() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private inline fun <reified VM : ViewModel> Fragment.lazyViewModel(
        noinline create: (List<QueryType?>) -> ViewModel
    ) = viewModels<VM> {
        MainViewModelFactory(listsOnScreenByQueryTypes, create)
    }

    override val viewModel: MainViewModel by lazyViewModel { lazyQueryList ->
        (requireContext().applicationContext as App).appComponent.mainViewModelFactory()
            .create(lazyQueryList)
    }

    // Виды списков, по которым будет строиться главный экран, если null, то случайный список
    private val listsOnScreenByQueryTypes: List<QueryType?> by lazy {
        listOf(
            QueryType.Premieres(getString(R.string.premieres_title)),
            QueryType.Top(getString(R.string.popular_title), TopList.TOP_100_POPULAR_FILMS),
            null,
            QueryType.Top(getString(R.string.top_250_title), TopList.TOP_250_BEST_FILMS),
            null,
            QueryType.ByParams(
                title = getString(R.string.serials_title),
                typeFilm = TypeFilm.TV_SERIES
            )
        )
    }

    // Коллекция списков на экране
    private val previewLists = mutableListOf<HorizontalFilmListView>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        previewLists.clear()

        for (queryType in listsOnScreenByQueryTypes) {
            val title = queryType?.title ?: getString(R.string.random_film_list_title)

            val filmList = HorizontalFilmListView(requireContext()).apply { setTitle(title) }
            binding.mainLayout.addView(filmList)
            previewLists.add(filmList)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//
//        activity?.window?.decorView?.let {
//            ViewCompat.setOnApplyWindowInsetsListener(it) { _, insets ->
//                _binding?.root?.updatePadding(top = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top)
//                _binding?.root?.updatePadding(top = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top)
//                insets
//            }
//            binding.root.requestApplyInsets()
//        }

        viewModel.mainScreenListsFlow.onEach { (position, response) ->
            val queryType = listsOnScreenByQueryTypes[position]
            if (queryType != null)
                fillLists(position, response, queryType)
            else {
                // Если требовался случайный список, то получаем из отдельного flow параметры случайного списка
                viewModel.randomGenresAndCountriesInQuery
                    .filter { it.first == position }
                    .take(1)
                    .onEach { (_, genre, country) ->
                        val title = getString(
                            R.string.genre_and_country_title,
                            genre.replaceFirstChar { it.uppercase() },
                            country
                        )
                        previewLists[position].setTitle(title)
                        val queryTypeAfterRandom =
                            QueryType.ByParams(title = title, genre = genre, country = country)
                        fillLists(position, response, queryTypeAfterRandom)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun fillLists(
        position: Int,
        filmList: ResponseList<Film>,
        queryType: QueryType
    ) {
        val viewToFill = previewLists[position]
        // Проверяем настроены ли адаптеры и при необходимости настраиваем их
        if (!viewToFill.isAdaptersSet) {
            val onClickItem: (Int) -> Unit = { kinopoiskId ->
                val action =
                    MainFragmentDirections.actionMainFragmentToFilmInfoFragment(kinopoiskId)
                findNavController().navigate(action)
            }
            val onClickLinkToAll: (() -> Unit)? = if (filmList.totalPages > 1) {
                {
                    val action = MainFragmentDirections
                        .actionMainFragmentToFilmListFragment(queryType)
                    findNavController().navigate(action)
                }
            } else // Если одна страница, то оставляем пустым - футер не будет показан
                null

            viewToFill.ListWithLinkBuilder(onClickItem)
                .setOnClickLinkToAll(onClickLinkToAll)
                .setClickFooterSameAsLinkToAll()
                .build()
        }

        viewToFill.submitData(filmList.items)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}