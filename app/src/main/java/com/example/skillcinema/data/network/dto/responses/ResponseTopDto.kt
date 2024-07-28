package com.example.skillcinema.data.network.dto.responses

import com.example.skillcinema.data.network.dto.film.FilmFromTopDto
import com.example.skillcinema.entity.response.ResponseList
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseTopDto(
    @Json(name = "pagesCount")
    override val totalPages: Int,
    @Json(name = "films")
    override val items: List<FilmFromTopDto>
) : ResponseList<FilmFromTopDto>()