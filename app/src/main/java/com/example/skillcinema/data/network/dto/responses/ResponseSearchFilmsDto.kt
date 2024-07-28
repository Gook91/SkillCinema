package com.example.skillcinema.data.network.dto.responses

import com.example.skillcinema.data.network.dto.film.FilmDto
import com.example.skillcinema.entity.response.ResponseList
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseSearchFilmsDto(
    @Json(name = "total")
    override val total: Int,
    @Json(name = "totalPages")
    override val totalPages: Int,
    @Json(name = "items")
    override val items: List<FilmDto>,
) : ResponseList<FilmDto>()