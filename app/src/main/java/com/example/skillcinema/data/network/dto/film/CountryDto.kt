package com.example.skillcinema.data.network.dto.film

import com.example.skillcinema.entity.film.Country
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CountryDto(
    @Json(name = "id")
    override var id: Int? = null,
    @Json(name = "country")
    override val country: String
) : Country()
