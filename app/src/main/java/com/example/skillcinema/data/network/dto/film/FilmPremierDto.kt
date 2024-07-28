package com.example.skillcinema.data.network.dto.film

import com.example.skillcinema.entity.film.Film
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FilmPremierDto(
    @Json(name = "kinopoiskId")
    override val kinopoiskId: Int,
    @Json(name = "nameRu")
    override val nameRu: String? = null,
    @Json(name = "nameEn")
    override val nameEn: String? = null,
    @Json(name = "year")
    override val year: Int,
    @Json(name = "posterUrl")
    override val posterUrl: String,
    @Json(name = "posterUrlPreview")
    override val posterUrlPreview: String,
    @Json(name = "countries")
    override val countries: List<CountryDto>,
    @Json(name = "genres")
    override val genres: List<GenreDto>,
    @Json(name = "duration")
    override val duration: Int? = null,
    @Json(name = "premiereRu")
    override val premiereRu: String,
) : Film()