package com.example.skillcinema.data.network.dto.film

import com.example.skillcinema.entity.film.Season
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SeasonDto (
    @Json(name = "number")
    override val number: Int,
    @Json(name = "episodes")
    override val episodes: List<EpisodeDto>
) : Season()