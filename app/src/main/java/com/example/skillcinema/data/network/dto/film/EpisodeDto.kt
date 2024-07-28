package com.example.skillcinema.data.network.dto.film

import com.example.skillcinema.entity.film.Episode
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EpisodeDto(
    @Json(name = "seasonNumber")
    override val seasonNumber: Int,
    @Json(name = "episodeNumber")
    override val episodeNumber: Int,
    @Json(name = "nameRu")
    override val nameRu: String?,
    @Json(name = "nameEn")
    override val nameEn: String?,
    @Json(name = "synopsis")
    override val synopsis: String?,
    @Json(name = "releaseDate")
    override val releaseDate: String?
) : Episode()
