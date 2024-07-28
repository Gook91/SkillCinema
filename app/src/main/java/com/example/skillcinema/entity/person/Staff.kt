package com.example.skillcinema.entity.person

import com.example.skillcinema.entity.film.Film

abstract class Staff {
    abstract val personId: Int
    open val webUrl: String? = null
    open val nameRu: String? = null
    open val nameEn: String? = null
    open val description: String? = null
    open val sex: String? = null
    abstract val posterUrl: String
    open val growth: String? = null
    open val birthday: String? = null
    open val death: String? = null
    open val age: Int? = null
    open val birthplace: String? = null
    open val deathplace: String? = null
    open val hasAwards: Int? = null
    open val profession: String? = null
    open val professionKey: String? = null
    open val facts: List<String>? = null
    open val films: List<Film>? = null
}