package com.example.skillcinema.data

import com.example.skillcinema.data.db.FilmStateDao
import com.example.skillcinema.data.db.FilmsDao
import com.example.skillcinema.data.db.dto.film.FilmLocalDto
import com.example.skillcinema.data.network.FilmsApi
import com.example.skillcinema.data.network.dto.film.FilmPremierDto
import com.example.skillcinema.entity.response.ResponseList
import com.example.skillcinema.entity.film.*
import com.example.skillcinema.entity.film.types.GalleryType
import com.example.skillcinema.entity.response.TopList
import com.example.skillcinema.entity.film.types.TypeFilm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.ceil
import kotlin.math.roundToInt

class RepositoryFilms @Inject constructor(
    private val filmsApi: FilmsApi,
    private val filmsDao: FilmsDao,
    private val filmStateDao: FilmStateDao,
    private val memoryStorage: MemoryStorage
) {

    // Списки жанров и фильмов из памяти или с сервера

    suspend fun getGenres(): List<Genre> = memoryStorage.genres ?: genresAndCountriesFactory()

    suspend fun getCountries(): List<Country> =
        memoryStorage.countries ?: genresAndCountriesFactory()

    private suspend inline fun <reified T> genresAndCountriesFactory(): List<T> {
        val response = filmsApi.getAllGenresAndCountries()
        memoryStorage.genres = response.genres
        memoryStorage.countries = response.countries
        @Suppress("UNCHECKED_CAST")
        return when (T::class) {
            Genre::class -> memoryStorage.genres as List<T>
            Country::class -> memoryStorage.countries as List<T>
            else -> emptyList()
        }
    }

    // Информация о фильме

    suspend fun getFilmInfo(filmId: Int): Film =
        memoryStorage.films.getOrPut(filmId) {
            filmsDao.getFilm(filmId)?.toFilm() ?: filmsApi.getFilmInfo(filmId).also { filmDto ->
                filmsDao.addFilm(FilmLocalDto(filmDto))
            }
        }

    fun getFilmStateFlow(kinopoiskId: Int): Flow<FilmState> {
        val isLikedFlow = filmStateDao.isLikedFilm(kinopoiskId)
        val isWatchLaterFlow = filmStateDao.isWatchLaterFilm(kinopoiskId)
        val isViewedFlow = filmStateDao.isViewedFilm(kinopoiskId)
        val filmStateFlow = combine(
            isLikedFlow,
            isWatchLaterFlow,
            isViewedFlow
        ) { isLiked, isWatchLater, isViewed ->
            FilmState(kinopoiskId, isLiked, isWatchLater, isViewed)
        }
        return filmStateFlow
    }

    suspend fun getSeasonsAndSeries(kinopoiskId: Int): List<Season> =
        memoryStorage.seasonsFromFilm.getOrPut(kinopoiskId) {
            filmsDao.getSeasonsByFilm(kinopoiskId)
                ?: filmsApi.getSeasonsAndSeries(kinopoiskId).items.also { seasons ->
                    filmsDao.addSeasonsFromFilm(kinopoiskId, seasons)
                }
        }

    suspend fun getImagesFromFilm(
        kinopoiskId: Int,
        type: GalleryType,
        page: Int = FIRST_PAGE
    ): ResponseList<Image> = filmsApi.getGalleryFilm(kinopoiskId, type.name, page)

    suspend fun getGalleryTypesWithCountFromFilm(kinopoiskId: Int): Map<GalleryType, Int> =
        memoryStorage.galleryTypesOfFilm.getOrPut(kinopoiskId) {
            val typeList = mutableMapOf<GalleryType, Int>()
            for (type in GalleryType.values()) {
                val response = getImagesFromFilm(kinopoiskId, type)
                response.total?.let { if (it > 0) typeList[type] = it }
            }
            typeList.toMap()
        }

    // Различные списки фильмов

    suspend fun getTopFilms(topList: TopList, page: Int = FIRST_PAGE): ResponseList<Film> =
        filmsApi.getTopFilms(topList.name, page).apply {
            val idViewedFilms = filmStateDao.getIdViewedFilms()
            items.onEach { film -> film.isViewed = film.kinopoiskId in idViewedFilms }
        }

    suspend fun getFilmsByParams(
        country: String? = null,
        genre: String? = null,
        order: String? = null,
        typeFilm: TypeFilm? = null,
        ratingFrom: Int? = null,
        ratingTo: Int? = null,
        yearFrom: Int? = null,
        yearTo: Int? = null,
        keyword: String? = null,
        page: Int = FIRST_PAGE,
    ): ResponseList<Film> = filmsApi.searchFilms(
        page,
        getCountries().find { it.country == country }?.id,
        getGenres().find { it.genre == genre }?.id,
        order,
        typeFilm?.name,
        ratingFrom,
        ratingTo,
        yearFrom,
        yearTo,
        keyword
    ).apply {
        val idViewedFilms = filmStateDao.getIdViewedFilms()
        items.onEach { film -> film.isViewed = film.kinopoiskId in idViewedFilms }
    }

    suspend fun getPremiereFilms(page: Int = FIRST_PAGE): ResponseList<Film> {
        val responseList = memoryStorage.premiereList ?: premiereListFactory().also {
            memoryStorage.premiereList = it
        }
        return createResponseList(page, responseList)
    }

    private suspend fun premiereListFactory(): List<List<FilmPremierDto>> {
        val currentDate = Calendar.getInstance()
        val endDateForPremiereList =
            Calendar.getInstance().apply { add(Calendar.DATE, COUNT_NEEDED_PREMIERE_DAYS) }

        val tempPremiereList = mutableListOf<FilmPremierDto>()
        val monthsBetweenDates =
            (endDateForPremiereList.get(Calendar.YEAR) - currentDate.get(Calendar.YEAR)) * 12 +
                    endDateForPremiereList.get(Calendar.MONTH) - currentDate.get(Calendar.MONTH)

        for (i in 0..monthsBetweenDates) {
            val monthForQuery = Calendar.getInstance().apply { add(Calendar.MONTH, i) }
            tempPremiereList += filmsApi.getPremieres(
                monthForQuery.get(Calendar.YEAR), queryMonthNameFormat.format(monthForQuery.time)
            ).items
        }

        tempPremiereList.removeAll { film ->
            val filmPremiereDate = premiereDateFormat.parse(film.premiereRu)?.time ?: 0
            filmPremiereDate !in (currentDate.timeInMillis..endDateForPremiereList.timeInMillis)
        }

        return pagingListFactory(tempPremiereList)
    }

    suspend fun getSimilarFilms(kinopoiskId: Int, page: Int = FIRST_PAGE): ResponseList<Film> {
        val listFilms = memoryStorage.similarFilmsById.getOrPut(kinopoiskId) {
            val rawResponse = filmsApi.getSimilarFilms(kinopoiskId).apply {
                val idViewedFilms = filmStateDao.getIdViewedFilms()
                items.onEach { film -> film.isViewed = film.kinopoiskId in idViewedFilms }
            }
            pagingListFactory(rawResponse.items)
        }
        return createResponseList(page, listFilms)
    }

    companion object {
        const val FIRST_PAGE = 1
        private const val ITEMS_PER_PAGE = 20
        val premiereDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        private val queryMonthNameFormat = SimpleDateFormat("MMMM", Locale.US)
        private const val COUNT_NEEDED_PREMIERE_DAYS = 14

        // Функция для создания матрицы фильмов для постраничного вывода
        private fun <T> pagingListFactory(sourceDataList: List<T>): List<List<T>> {
            val pagedList = mutableListOf<List<T>>()
            val pageCount =
                ceil(sourceDataList.size / ITEMS_PER_PAGE.toDouble()).roundToInt()

            for (i in 0 until pageCount) {
                pagedList.add(
                    sourceDataList.subList(
                        i * ITEMS_PER_PAGE,
                        // Берём меньшее из номера последнего элемента страницы и последнего индекса массива,
                        // иначе получим IndexOutOfBoundsException
                        minOf((i + 1) * ITEMS_PER_PAGE - 1, sourceDataList.lastIndex)
                    )
                )
            }
            return pagedList
        }

        // Функция для создания объекта ответа
        private fun <T> createResponseList(page: Int, pagedList: List<List<T>>): ResponseList<T> {
            val pageFilms = pagedList.getOrNull(page - FIRST_PAGE) ?: emptyList()
            val pageCount = pagedList.size
            return ResponseList(
                totalPages = pageCount,
                items = pageFilms
            )
        }
    }
}