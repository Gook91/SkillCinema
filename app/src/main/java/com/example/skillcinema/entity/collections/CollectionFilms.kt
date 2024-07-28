package com.example.skillcinema.entity.collections

open class CollectionFilms(
    val type: TypeCollection,
    open val countFilms: Int? = null,
    open var isMovieInCollection: Boolean? = null
)