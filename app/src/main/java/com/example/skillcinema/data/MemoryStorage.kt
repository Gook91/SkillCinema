package com.example.skillcinema.data

import com.example.skillcinema.data.network.dto.film.FilmPremierDto
import com.example.skillcinema.data.network.dto.film.FilmSimilarDto
import com.example.skillcinema.entity.film.Country
import com.example.skillcinema.entity.film.Film
import com.example.skillcinema.entity.film.Genre
import com.example.skillcinema.entity.film.Season
import com.example.skillcinema.entity.person.Staff
import com.example.skillcinema.entity.film.types.GalleryType
import java.text.SimpleDateFormat
import java.util.*

// Объект со списками, сохранёнными в памяти
object MemoryStorage {

    // Переменные списка жанров и фильмов
    var genres: List<Genre>? = null
    var countries: List<Country>? = null

    // Строка с текущей датой
    private val currentStringDate: String = SimpleDateFormat(
        "yyyy-MM-dd",
        Locale.US
    ).format(Calendar.getInstance().time)

    // Постраничный список с премьерами и дата его последнего обновления
    private var dateLastQueryPremieres: String? = null
    var premiereList: List<List<FilmPremierDto>>? = null
        get() {
            return if (
            // Если списка премьер нет или дата его обновления не сегодня, то возвращаем null
                field.isNullOrEmpty() ||
                currentStringDate != dateLastQueryPremieres
            )
                null
            // Если список есть и дата создания - сегодня, то возвращаем его
            else field
        }
        // При установке списка обновляем дату его сохранения
        set(value) {
            dateLastQueryPremieres = currentStringDate
            field = value
        }

    // Информация о фильме по id
    val films = mutableMapOf<Int, Film>()

    // Коллекция похожих фильмов по заданному id
    val similarFilmsById = mutableMapOf<Int, List<List<FilmSimilarDto>>>()

    // Коллекция сезонов серий определённого фильма
    val seasonsFromFilm = mutableMapOf<Int, List<Season>>()

    // Коллекция людей, задействованных в съёмках конкретного фильма
    val staffFromFilm = mutableMapOf<Int, List<Staff>>()

    // Коллекция информации о людях по их id
    val staff = mutableMapOf<Int, Staff>()

    // Список типов галерей для фильма с количеством изображений для каждого типа
    val galleryTypesOfFilm = mutableMapOf<Int, Map<GalleryType, Int>>()
}