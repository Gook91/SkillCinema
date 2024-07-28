package com.example.skillcinema.data.network.dto.responses

import com.example.skillcinema.data.network.dto.film.CountryDto
import com.example.skillcinema.data.network.dto.film.GenreDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseFiltersDto(
    @Json(name = "genres")
    val genres: List<GenreDto>,
    @Json(name = "countries")
    val countries: List<CountryDto>
)
