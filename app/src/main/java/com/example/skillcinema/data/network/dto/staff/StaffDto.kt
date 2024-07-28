package com.example.skillcinema.data.network.dto.staff

import com.example.skillcinema.entity.person.Staff
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StaffDto(
    @Json(name = "personId")
    override val personId: Int,
    @Json(name = "webUrl")
    override val webUrl: String? = null,
    @Json(name = "nameRu")
    override val nameRu: String? = null,
    @Json(name = "nameEn")
    override val nameEn: String? = null,
    @Json(name = "sex")
    override val sex: String? = null,
    @Json(name = "posterUrl")
    override val posterUrl: String,
    @Json(name = "growth")
    override val growth: String? = null,
    @Json(name = "birthday")
    override val birthday: String? = null,
    @Json(name = "death")
    override val death: String? = null,
    @Json(name = "age")
    override val age: Int? = null,
    @Json(name = "birthplace")
    override val birthplace: String? = null,
    @Json(name = "deathplace")
    override val deathplace: String? = null,
    @Json(name = "hasAwards")
    override val hasAwards: Int? = null,
    @Json(name = "profession")
    override val profession: String? = null,
    @Json(name = "facts")
    override val facts: List<String>,
    @Json(name = "films")
    override val films: List<FilmsFromStaffDto>
) : Staff()