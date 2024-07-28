package com.example.skillcinema.data.network.dto.staff

import com.example.skillcinema.entity.film.Film
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FilmsFromStaffDto(
    @Json(name = "filmId") // Отличие только в строчке id
    override val kinopoiskId: Int,
    @Json(name = "nameRu")
    override val nameRu: String? = null,
    @Json(name = "nameEn")
    override val nameEn: String? = null,
    @Json(name = "rating")
    override val ratingKinopoisk: Double? = null,
    @Json(name = "description")
    override val personDescription: String? = null,
    @Json(name = "professionKey")
    override val professionKey: String,
) : Film() {
    override val posterUrl =
        "https://kinopoiskapiunofficial.tech/images/posters/kp/$kinopoiskId.jpg"
    override val posterUrlPreview =
        "https://kinopoiskapiunofficial.tech/images/posters/kp_small/$kinopoiskId.jpg"
}