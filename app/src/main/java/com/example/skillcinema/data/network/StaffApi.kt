package com.example.skillcinema.data.network

import com.example.skillcinema.data.network.dto.staff.StaffDto
import com.example.skillcinema.data.network.dto.staff.StaffFromFilmDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StaffApi {
    @GET("/api/v1/staff")
    suspend fun getPersonsFromFilm(@Query("filmId") kinopoiskId: Int): Response<List<StaffFromFilmDto>>

    @GET("/api/v1/staff/{staffId}")
    suspend fun getStaffInfo(@Path("staffId") staffId: Int):StaffDto
}