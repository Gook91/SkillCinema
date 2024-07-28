package com.example.skillcinema.entity.query

import android.os.Parcelable
import com.example.skillcinema.entity.response.TopList
import com.example.skillcinema.entity.film.types.TypeFilm
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class QueryType : Parcelable {
    abstract val title: String

    data class Top(
        override val title: String,
        val type: TopList
    ) : QueryType()

    data class ByParams(
        override val title: String,
        val country: String? = null,
        val genre: String? = null,
        val order: TypeSort? = null,
        val typeFilm: TypeFilm? = null,
        val ratingFrom: Int? = null,
        val ratingTo: Int? = null,
        val yearFrom: Int? = null,
        val yearTo: Int? = null,
        val keyword: String? = null,
        val isShowNotViewedFilms: Boolean = false
    ) : QueryType()

    data class Premieres(
        override val title: String
    ) : QueryType()

    data class Similars(
        override val title: String,
        val id: Int
    ) : QueryType()
}