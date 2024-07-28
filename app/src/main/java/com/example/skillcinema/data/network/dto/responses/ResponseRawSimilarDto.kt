package com.example.skillcinema.data.network.dto.responses

import com.example.skillcinema.data.network.dto.film.FilmSimilarDto
import com.example.skillcinema.entity.response.ResponseList
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseRawSimilarDto(
    @Json(name = "total")
    override val total: Int,
    @Json(name = "items")
    override val items: List<FilmSimilarDto>
) : ResponseList<FilmSimilarDto>()