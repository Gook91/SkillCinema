package com.example.skillcinema.data.db.dto.film

import androidx.room.*
import com.example.skillcinema.entity.film.Country
import com.example.skillcinema.entity.film.Film
import com.example.skillcinema.entity.film.Genre

@Entity(tableName = "films")
@TypeConverters(ListToStringConverter::class)
data class FilmLocalDto(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "kinopoisk_id")
    val kinopoiskId: Int,
    @ColumnInfo(name = "name_ru")
    val nameRu: String? = null,
    @ColumnInfo(name = "name_en")
    val nameEn: String? = null,
    @ColumnInfo(name = "name_original")
    val nameOriginal: String? = null,
    @ColumnInfo(name = "logo_url")
    val logoUrl: String? = null,
    @ColumnInfo(name = "rating_kinopoisk")
    val ratingKinopoisk: Double? = null,
    @ColumnInfo(name = "year")
    val year: Int? = null,
    @ColumnInfo(name = "genres")
    val genres: List<Genre>? = null,
    @ColumnInfo(name = "countries")
    val countries: List<Country>? = null,
    @ColumnInfo(name = "film_length")
    val filmLength: String? = null,
    @ColumnInfo(name = "rating_age_limits")
    val ratingAgeLimits: String? = null,
    @ColumnInfo(name = "short_description")
    val shortDescription: String? = null,
    @ColumnInfo(name = "description")
    val description: String? = null,
    @ColumnInfo(name = "type")
    val type: String? = null,
    @ColumnInfo(name = "poster_url")
    val posterUrl: String,
    @ColumnInfo(name = "poster_url_preview")
    val posterUrlPreview: String
) {

    constructor(film: Film) : this(
        kinopoiskId = film.kinopoiskId,
        nameRu = film.nameRu,
        nameEn = film.nameEn,
        nameOriginal = film.nameOriginal,
        logoUrl = film.logoUrl,
        ratingKinopoisk = film.ratingKinopoisk,
        year = film.year,
        genres = film.genres,
        countries = film.countries,
        filmLength = film.filmLength,
        ratingAgeLimits = film.ratingAgeLimits,
        shortDescription = film.shortDescription,
        description = film.description,
        type = film.type,
        posterUrl = film.posterUrl,
        posterUrlPreview = film.posterUrlPreview,
    )

    fun toFilm(): Film {
        return object : Film() {
            override val kinopoiskId = this@FilmLocalDto.kinopoiskId
            override val nameRu = this@FilmLocalDto.nameRu
            override val nameEn = this@FilmLocalDto.nameEn
            override val nameOriginal = this@FilmLocalDto.nameOriginal
            override val logoUrl = this@FilmLocalDto.logoUrl
            override val ratingKinopoisk: Double? = this@FilmLocalDto.ratingKinopoisk
            override val year: Int? = this@FilmLocalDto.year
            override val genres: List<Genre>? = this@FilmLocalDto.genres
            override val countries: List<Country>? = this@FilmLocalDto.countries
            override val filmLength = this@FilmLocalDto.filmLength
            override val ratingAgeLimits = this@FilmLocalDto.ratingAgeLimits
            override val shortDescription = this@FilmLocalDto.shortDescription
            override val description = this@FilmLocalDto.description
            override val type = this@FilmLocalDto.type
            override val posterUrl = this@FilmLocalDto.posterUrl
            override val posterUrlPreview = this@FilmLocalDto.posterUrlPreview
        }
    }
}
