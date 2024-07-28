package com.example.skillcinema.data.network.dto.film

import com.example.skillcinema.entity.film.Genre
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GenreDto(
    @Json(name = "id")
    override val id: Int? = null,
    @Json(name = "genre")
    override val genre: String
) : Genre()