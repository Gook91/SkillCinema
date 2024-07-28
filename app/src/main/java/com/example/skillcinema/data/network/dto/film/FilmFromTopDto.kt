package com.example.skillcinema.data.network.dto.film

import com.example.skillcinema.entity.film.Film
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class FilmFromTopDto(
    @Json(name = "filmId") // Отличие в строчке id
    override val kinopoiskId: Int,
    @Json(name = "nameRu")
    override val nameRu: String? = null,
    @Json(name = "nameEn")
    override val nameEn: String? = null,
    @Json(name = "year")
    override val year: Int? = null,
    @Json(name = "filmLength")
    override val filmLength: String? = null,
    @Json(name = "countries")
    override val countries: List<CountryDto>,
    @Json(name = "genres")
    override val genres: List<GenreDto>,
    @Json(name = "rating") // Отличие в строчке рейтинга
    val rating: String? = null,
    @Json(name = "ratingVoteCount") // Отличие в строчке количества голосов рейтинга
    override val ratingKinopoiskVoteCount: Int? = null,
    @Json(name = "posterUrl")
    override val posterUrl: String,
    @Json(name = "posterUrlPreview")
    override val posterUrlPreview: String,
) : Film() {
    override val ratingKinopoisk: Double? = rating?.toDoubleOrNull()
}