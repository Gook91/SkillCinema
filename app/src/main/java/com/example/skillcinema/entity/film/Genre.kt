package com.example.skillcinema.entity.film

abstract class Genre {
    open val id: Int? = null
    abstract val genre: String
}