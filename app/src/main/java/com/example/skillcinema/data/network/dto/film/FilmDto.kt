package com.example.skillcinema.data.network.dto.film

import com.example.skillcinema.entity.film.Film
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class FilmDto(
    @Json(name = "kinopoiskId")
    override val kinopoiskId: Int,
    @Json(name = "imdbId")
    override val imdbId: String? = null,
    @Json(name = "nameRu")
    override val nameRu: String? = null,
    @Json(name = "nameEn")
    override val nameEn: String? = null,
    @Json(name = "nameOriginal")
    override val nameOriginal: String? = null,
    @Json(name = "posterUrl")
    override val posterUrl: String,
    @Json(name = "posterUrlPreview")
    override val posterUrlPreview: String,
    @Json(name = "coverUrl")
    override val coverUrl: String? = null,
    @Json(name = "logoUrl")
    override val logoUrl: String? = null,
    @Json(name = "reviewsCount")
    override val reviewsCount: Int? = null,
    @Json(name = "ratingGoodReview")
    override val ratingGoodReview: Double? = null,
    @Json(name = "ratingGoodReviewVoteCount")
    override val ratingGoodReviewVoteCount: Int? = null,
    @Json(name = "ratingKinopoisk")
    override val ratingKinopoisk: Double? = null,
    @Json(name = "ratingKinopoiskVoteCount")
    override val ratingKinopoiskVoteCount: Int? = null,
    @Json(name = "ratingImdb")
    override val ratingImdb: Double? = null,
    @Json(name = "ratingImdbVoteCount")
    override val ratingImdbVoteCount: Int? = null,
    @Json(name = "ratingFilmCritics")
    override val ratingFilmCritics: Double? = null,
    @Json(name = "ratingFilmCriticsVoteCount")
    override val ratingFilmCriticsVoteCount: Int? = null,
    @Json(name = "ratingAwait")
    override val ratingAwait: Double? = null,
    @Json(name = "ratingAwaitCount")
    override val ratingAwaitCount: Int? = null,
    @Json(name = "ratingRfCritics")
    override val ratingRfCritics: Double? = null,
    @Json(name = "ratingRfCriticsVoteCount")
    override val ratingRfCriticsVoteCount: Int? = null,
    @Json(name = "webUrl")
    override val webUrl: String? = null,
    @Json(name = "year")
    override val year: Int? = null,
    @Json(name = "filmLength")
    override val filmLength: String? = null,
    @Json(name = "slogan")
    override val slogan: String? = null,
    @Json(name = "description")
    override val description: String? = null,
    @Json(name = "shortDescription")
    override val shortDescription: String? = null,
    @Json(name = "editorAnnotation")
    override val editorAnnotation: String? = null,
    @Json(name = "isTicketsAvailable")
    override val isTicketsAvailable: Boolean? = null,
    @Json(name = "productionStatus")
    override val productionStatus: String? = null,
    @Json(name = "type")
    override val type: String? = null,
    @Json(name = "ratingMpaa")
    override val ratingMpaa: String? = null,
    @Json(name = "ratingAgeLimits")
    override val ratingAgeLimits: String? = null,
    @Json(name = "hasImax")
    override val hasImax: Boolean? = null,
    @Json(name = "has3D")
    override val has3D: Boolean? = null,
    @Json(name = "lastSync")
    override val lastSync: String? = null,
    @Json(name = "countries")
    override val countries: List<CountryDto>,
    @Json(name = "genres")
    override val genres: List<GenreDto>,
    @Json(name = "startYear")
    override val startYear: Int? = null,
    @Json(name = "endYear")
    override val endYear: Int? = null,
    @Json(name = "serial")
    override val serial: Boolean? = null,
    @Json(name = "shortFilm")
    override val shortFilm: Boolean? = null,
    @Json(name = "completed")
    override val completed: Boolean? = null
) : Film()