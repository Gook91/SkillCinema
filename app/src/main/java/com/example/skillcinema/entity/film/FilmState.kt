package com.example.skillcinema.entity.film

data class FilmState(
    val kinopoiskId: Int,
    val isLiked: Boolean,
    val isWatchLater: Boolean,
    val isViewed: Boolean
)