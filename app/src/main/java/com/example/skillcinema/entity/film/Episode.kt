package com.example.skillcinema.entity.film

abstract class Episode {
    abstract val seasonNumber: Int
    abstract val episodeNumber: Int
    open val nameRu: String? = null
    open val nameEn: String? = null
    open val synopsis: String? = null
    open val releaseDate: String? = null
}