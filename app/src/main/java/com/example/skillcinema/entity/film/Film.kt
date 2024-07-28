package com.example.skillcinema.entity.film

// Сущность фильма
abstract class Film {
    abstract val kinopoiskId: Int
    open val imdbId: String? = null
    open val nameRu: String? = null
    open val nameEn: String? = null
    open val nameOriginal: String? = null
    abstract val posterUrl: String
    abstract val posterUrlPreview: String
    open val coverUrl: String? = null
    open val logoUrl: String? = null
    open val reviewsCount: Int? = null
    open val ratingGoodReview: Double? = null
    open val ratingGoodReviewVoteCount: Int? = null
    open val ratingKinopoisk: Double? = null
    open val ratingKinopoiskVoteCount: Int? = null
    open val ratingImdb: Double? = null
    open val ratingImdbVoteCount: Int? = null
    open val ratingFilmCritics: Double? = null
    open val ratingFilmCriticsVoteCount: Int? = null
    open val ratingAwait: Double? = null
    open val ratingAwaitCount: Int? = null
    open val ratingRfCritics: Double? = null
    open val ratingRfCriticsVoteCount: Int? = null
    open val webUrl: String? = null
    open val year: Int? = null
    open val filmLength: String? = null
    open val slogan: String? = null
    open val description: String? = null
    open val shortDescription: String? = null
    open val editorAnnotation: String? = null
    open val isTicketsAvailable: Boolean? = null
    open val productionStatus: String? = null
    open val type: String? = null
    open val ratingMpaa: String? = null
    open val ratingAgeLimits: String? = null
    open val hasImax: Boolean? = null
    open val has3D: Boolean? = null
    open val lastSync: String? = null
    open val countries: List<Country>? = null
    open val genres: List<Genre>? = null
    open val startYear: Int? = null
    open val endYear: Int? = null
    open val serial: Boolean? = null
    open val shortFilm: Boolean? = null
    open val completed: Boolean? = null
    open val premiereRu: String? = null
    open val duration: Int? = null

    // Для списков фильмов
    open var isViewed: Boolean = false

    // Для списка фильмов у персоны:
    open val personDescription: String? = null
    open val professionKey: String? = null
}