package com.example.skillcinema.entity.film

abstract class Season {
    abstract val number: Int
    abstract val episodes: List<Episode>
}