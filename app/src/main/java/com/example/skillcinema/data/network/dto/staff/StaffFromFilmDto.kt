package com.example.skillcinema.data.network.dto.staff

import com.example.skillcinema.entity.person.Staff
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StaffFromFilmDto(
    @Json(name = "staffId")
    override val personId: Int,
    @Json(name = "nameRu")
    override val nameRu: String?,
    @Json(name = "nameEn")
    override val nameEn: String?,
    @Json(name = "description")
    override val description: String?,
    @Json(name = "posterUrl")
    override val posterUrl: String,
    @Json(name = "professionText")
    override val profession: String?,
    @Json(name = "professionKey")
    override val professionKey: String?,
): Staff()
