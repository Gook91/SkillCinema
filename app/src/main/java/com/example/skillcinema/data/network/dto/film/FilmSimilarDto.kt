package com.example.skillcinema.data.network.dto.film

import com.example.skillcinema.entity.film.Film
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FilmSimilarDto(
    @Json(name = "filmId")
    override val kinopoiskId: Int,
    @Json(name = "nameRu")
    override val nameRu: String?,
    @Json(name = "nameEn")
    override val nameEn: String?,
    @Json(name = "nameOriginal")
    override val nameOriginal: String?,
    @Json(name = "posterUrl")
    override val posterUrl: String,
    @Json(name = "posterUrlPreview")
    override val posterUrlPreview: String
): Film()
