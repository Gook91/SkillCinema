package com.example.skillcinema.presentation.screen.filmInfo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.text.parseAsHtml
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.skillcinema.App
import com.example.skillcinema.R
import com.example.skillcinema.databinding.FragmentFilmInfoBinding
import com.example.skillcinema.entity.response.ResponseList
import com.example.skillcinema.entity.film.Film
import com.example.skillcinema.entity.film.FilmState
import com.example.skillcinema.entity.film.Image
import com.example.skillcinema.entity.person.Staff
import com.example.skillcinema.entity.query.QueryType
import com.example.skillcinema.presentation.customViews.horizontalListView.HorizontalStaffListView
import com.example.skillcinema.presentation.dialogs.collections.CollectionsDialogFragment
import com.example.skillcinema.presentation.screen.abstractTemplates.AbstractFragmentWithErrorMessageAndInsets
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class FilmInfoFragment : AbstractFragmentWithErrorMessageAndInsets() {


    private var _binding: FragmentFilmInfoBinding? = null
    private val binding get() = _binding!!

    private val args: FilmInfoFragmentArgs by navArgs()
    val kinopoiskId: Int by lazy { args.kinopoiskId }

    private inline fun <reified VM : ViewModel> Fragment.lazyViewModel(
        noinline create: (Int) -> VM
    ) = viewModels<VM> {
        FilmInfoViewModelFactory(kinopoiskId, create)
    }

    override val viewModel: FilmInfoViewModel by lazyViewModel { lazyKinopoiskId ->
        (requireContext().applicationContext as App).appComponent.filmInfoViewModelFactory()
            .create(lazyKinopoiskId)
    }

    override fun updateTopPaddingFromInsets(rootView: View) = Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.filmInfoFlow.filterNotNull().onEach { film ->
            setMainData(film)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.filmStateFlow.filterNotNull().onEach { filmState ->
            setFilmState(filmState)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.actorsFromFilmFlow.filterNotNull().onEach { actorsList ->
            setDataInPersonList(actorsList, binding.actorsList, isActors = true)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.personsFromFilmFlow.filterNotNull().onEach { personsList ->
            setDataInPersonList(personsList, binding.staffList, isActors = false)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.galleryFlow.filterNotNull().onEach { galleryList ->
            setDataInGalleryList(galleryList)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.similarFilmsFlow.filterNotNull().onEach { filmList ->
            setDataInSimilarList(filmList)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.likedBox.setOnClickListener {
            viewModel.updateFilmState(
                isLiked = (it as CheckBox).isChecked
            )
        }

        binding.watchLaterBox.setOnClickListener {
            viewModel.updateFilmState(
                isWatchLater = (it as CheckBox).isChecked
            )
        }

        binding.viewedBox.setOnClickListener {
            viewModel.updateFilmState(
                isViewed = (it as CheckBox).isChecked
            )
        }
    }

    private fun setMainData(film: Film) {
        with(binding) {
            Glide.with(requireContext())
                .load(film.posterUrl)
                .into(posterView)

            val subTitle: String

            if (film.logoUrl != null) {
                Glide.with(requireContext())
                    .load(film.logoUrl)
                    .into(logoImage)
                logoText.visibility = View.GONE
                subTitle = film.nameRu ?: film.nameEn ?: film.nameOriginal ?: ""
            } else {
                when {
                    film.nameRu != null -> {
                        logoText.text = film.nameRu
                        subTitle = film.nameEn ?: film.nameOriginal ?: film.nameRu!!
                    }

                    film.nameEn != null -> {
                        logoText.text = film.nameEn
                        subTitle = film.nameOriginal ?: film.nameEn!!
                    }

                    else -> {
                        logoText.text = film.nameOriginal ?: ""
                        subTitle = film.nameOriginal ?: ""
                    }
                }
                logoImage.visibility = View.GONE
            }

            // Создаём ссылку, чтобы поделиться
            binding.shareButton.setOnClickListener {
                val shareUrl = if (film.imdbId != null)
                    IMDB_URL + film.imdbId
                else
                    KINOPOISK_URL + film.kinopoiskId
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, shareUrl)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }

            binding.collectionsButton.setOnClickListener {
                val collectionsDialog = CollectionsDialogFragment()
                val bundle =
                    bundleOf(CollectionsDialogFragment.KINOPOISK_ID_BUNDLE_TAG to kinopoiskId)
                collectionsDialog.arguments = bundle
                collectionsDialog.show(childFragmentManager, COLLECTIONS_DIALOG_SHOW_TAG)
            }

            // Получаем рейтинг и имя и выводим их с форматированием
            ratingName.text = if (film.ratingKinopoisk != null)
                getString(R.string.rating_and_name)
                    .format(film.ratingKinopoisk, subTitle)
                    .parseAsHtml()
            else
                subTitle

            val secondStringList = mutableListOf<String>()
            film.year?.let { secondStringList.add(it.toString()) }
            film.genres?.let { secondStringList.addAll(it.map { item -> item.genre }) }
            yearGenre.text = secondStringList.joinToString(SEPARATOR)

            val thirdStringList = mutableListOf<String>()
            film.countries?.let { thirdStringList.addAll(it.map { item -> item.country }) }
            film.filmLength?.let { lengthString ->
                try {
                    val length = lengthString.toInt()
                    val hours: Int = length / 60
                    var formattedLength =
                        if (hours > 0) getString(R.string.length_hours, hours) else ""
                    formattedLength += getString(R.string.length_minutes, length % 60)
                    thirdStringList.add(formattedLength)
                } catch (nfe: NumberFormatException) {
                    thirdStringList.add(lengthString)
                }
            }
            film.ratingAgeLimits?.let {
                thirdStringList.add(it.replace("age", "") + "+")
            }
            countryLengthAge.text = thirdStringList.joinToString(SEPARATOR)

            if (film.shortDescription != null)
                shortDescription.text = film.shortDescription
            else shortDescription.visibility = View.GONE

            if (film.description != null)
                film.description?.let { addCollapsingDescription(it) }
            else description.visibility = View.GONE

            if (film.type in FilmInfoViewModel.typeFilmsWithSeasons)
                viewModel.seasonsAndSeriesFlow.filterNotNull().onEach { (seasons, series) ->
                    setSeasons(seasons, series)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    private fun addCollapsingDescription(description: String) {
        var isCollapsed = true
        with(binding.description) {
            text = description
            maxLines = MAX_LINES_DESCRIPTION
            setOnClickListener {
                maxLines = if (isCollapsed) Int.MAX_VALUE else MAX_LINES_DESCRIPTION
                isCollapsed = !isCollapsed
            }
        }
    }

    private fun setFilmState(filmState: FilmState) {
        binding.likedBox.isChecked = filmState.isLiked
        binding.watchLaterBox.isChecked = filmState.isWatchLater
        binding.viewedBox.isChecked = filmState.isViewed
    }

    private fun setSeasons(seasons: Int, series: Int) {
        if (seasons == 0 || series == 0) return
        binding.seasonsInfoGroup.visibility = View.VISIBLE
        val seasonsString = resources.getQuantityString(R.plurals.seasons_count, seasons, seasons)
        val seriesString = resources.getQuantityString(R.plurals.series_count, series, series)
        val titleText = binding.yearGenre.text.toString() + SEPARATOR + seasonsString
        val seasonsInfo = seasonsString + SEPARATOR + seriesString
        binding.yearGenre.text = titleText
        binding.seasonsInfo.text = seasonsInfo
        binding.seasonToAllLink.setOnClickListener {
            val action =
                FilmInfoFragmentDirections.actionFilmInfoFragmentToEpisodesFromSerialFragment(
                    kinopoiskId
                )
            findNavController().navigate(action)
        }
    }

    private fun setDataInPersonList(
        staffList: List<Staff>,
        listView: HorizontalStaffListView,
        isActors: Boolean
    ) {
        if (staffList.isEmpty())
            listView.visibility = View.GONE
        else {
            val onClickItem: (Int) -> Unit = { staffId ->
                val action =
                    FilmInfoFragmentDirections.actionFilmInfoFragmentToStaffInfoFragment(staffId)
                findNavController().navigate(action)
            }
            var onClickFooter: (() -> Unit)? = null
            var countActors: Int? = null
            val maxCount = if (isActors) MAX_SHOWING_ACTORS else MAX_SHOWING_COUNT_NON_ACTORS
            if (staffList.size > maxCount) {
                onClickFooter = {
                    val action = FilmInfoFragmentDirections
                        .actionFilmInfoFragmentToStaffListFragment(kinopoiskId, isActors)
                    findNavController().navigate(action)
                }
                countActors = staffList.size
            }
            listView.setAndShowData(
                staffList.take(maxCount),
                onClickItem,
                onClickFooter,
                countActors
            )
        }
    }

    private fun setDataInGalleryList(
        imageList: ResponseList<Image>
    ) {
        if (imageList.items.isEmpty()) return
        binding.galleryList.visibility = View.VISIBLE
        val onClickItem: (Int) -> Unit = { position ->
            Toast.makeText(
                context,
                "Щелчок по картинке $position",
                Toast.LENGTH_SHORT
            ).show()
        }
        val onClickFooter: (() -> Unit) = {
            val action = FilmInfoFragmentDirections
                .actionFilmInfoFragmentToGalleryFromFilmFragment(kinopoiskId)
            findNavController().navigate(action)
        }
        binding.galleryList.setAndShowData(
            imageList.items,
            onClickItem,
            onClickFooter
        )
    }

    private fun setDataInSimilarList(filmList: ResponseList<Film>) {
        if (filmList.items.isEmpty()) {
            binding.similarFilmList.visibility = View.GONE
        } else {
            val list = filmList.items
            val onClickItem: (Int) -> Unit = { position ->
                val action =
                    FilmInfoFragmentDirections.actionFilmInfoFragmentToFilmInfoFragment(position)
                findNavController().navigate(action)
            }
            val onClickFooter: (() -> Unit)? = if (filmList.totalPages > 1) {
                {
                    val action =
                        FilmInfoFragmentDirections.actionFilmInfoFragmentToFilmListFragment(
                            QueryType.Similars(getString(R.string.similar_films), kinopoiskId)
                        )
                    findNavController().navigate(action)
                }
            } else
                null
            binding.similarFilmList.setAndShowData(list, onClickItem, onClickFooter)
        }
    }

//    override fun onResume() {
//        super.onResume()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            activity?.window?.setDecorFitsSystemWindows(false)
//        } else{
//            activity?.window?.let { WindowCompat.setDecorFitsSystemWindows(it, false) }
//        }
//    //let { WindowCompat.setDecorFitsSystemWindows(it, false) }
//    }

//    override fun onPause() {
//        super.onPause()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            activity?.window?.setDecorFitsSystemWindows(true)
//        } else{
//            activity?.window?.let { WindowCompat.setDecorFitsSystemWindows(it, true) }
//        }
//        //activity?.window?.let { WindowCompat.setDecorFitsSystemWindows(it, true) }
//    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val SEPARATOR = ", "
        private const val MAX_LINES_DESCRIPTION = 3
        private const val MAX_SHOWING_ACTORS = 20
        private const val MAX_SHOWING_COUNT_NON_ACTORS = 6
        private const val IMDB_URL = "https://www.imdb.com/title/"
        private const val KINOPOISK_URL = "https://www.kinopoisk.ru/film/"
        private const val COLLECTIONS_DIALOG_SHOW_TAG = "Collections dialog"
    }
}