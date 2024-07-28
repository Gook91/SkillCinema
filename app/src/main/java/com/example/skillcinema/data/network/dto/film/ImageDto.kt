package com.example.skillcinema.data.network.dto.film

import com.example.skillcinema.entity.film.Image
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageDto(
    @Json(name = "imageUrl")
    override val imageUrl: String,
    @Json(name = "previewUrl")
    override val previewUrl: String
) : Image()