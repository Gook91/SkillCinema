package com.example.skillcinema.data.network

import com.example.skillcinema.data.network.dto.film.FilmDto
import com.example.skillcinema.data.network.dto.responses.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FilmsApi {

    // Получаем список всех жанров и стран
    @GET("/api/v2.2/films/filters")
    suspend fun getAllGenresAndCountries(): ResponseFiltersDto

    // Получаем топ фильмов по типу
    @GET("/api/v2.2/films/top")
    suspend fun getTopFilms(@Query("type") type: String, @Query("page") page: Int): ResponseTopDto

    // Получаем список премьер по месяцу и году
    @GET("/api/v2.2/films/premieres")
    suspend fun getPremieres(
        @Query("year") year: Int,
        @Query("month") month: String
    ): ResponseRawPremieresDto

    // Получаем список фильмов по поисковому параметру
    @GET("/api/v2.2/films")
    suspend fun searchFilms(
        @Query("page") page: Int,
        @Query("countries") countries: Int? = null,
        @Query("genres") genres: Int? = null,
        @Query("order") order: String? = null,
        @Query("type") type: String? = null,
        @Query("ratingFrom") ratingFrom: Int? = null,
        @Query("ratingTo") ratingTo: Int? = null,
        @Query("yearFrom") yearFrom: Int? = null,
        @Query("yearTo") yearTo: Int? = null,
        @Query("keyword") keyword: String? = null,
    ): ResponseSearchFilmsDto

    // Получаем информацию о фильме
    @GET("/api/v2.2/films/{kinopoisk_id}")
    suspend fun getFilmInfo(@Path("kinopoisk_id") filmId: Int): FilmDto

    // Получаем список сезонов и серий
    @GET("/api/v2.2/films/{kinopoisk_id}/seasons")
    suspend fun getSeasonsAndSeries(@Path("kinopoisk_id") filmId: Int): ResponseSeasonsDto

    // Получаем список похожих фильмов
    @GET("/api/v2.2/films/{kinopoisk_id}/similars")
    suspend fun getSimilarFilms(@Path("kinopoisk_id") filmId: Int): ResponseRawSimilarDto

    // Получаем список изображений по типу
    @GET("/api/v2.2/films/{film_id}/images")
    suspend fun getGalleryFilm(
        @Path("film_id") filmId: Int,
        @Query("type") type: String,
        @Query("page") page: Int
    ): ResponseImageDto
}