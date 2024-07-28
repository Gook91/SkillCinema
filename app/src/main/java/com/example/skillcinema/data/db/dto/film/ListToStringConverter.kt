package com.example.skillcinema.data.db.dto.film

import androidx.room.TypeConverter
import com.example.skillcinema.entity.film.Country
import com.example.skillcinema.entity.film.Genre

class ListToStringConverter {
    @TypeConverter
    fun fromGenres(genres: List<Genre>): String =
        genres.joinToString(DB_SEPARATOR) { it.genre }

    @TypeConverter
    fun toGenres(data: String): List<Genre> {
        val list: List<Genre> = data.split(DB_SEPARATOR).map { item ->
            object : Genre() {
                override var genre: String = item
            }
        }
        return list
    }

    @TypeConverter
    fun fromCountries(countries: List<Country>): String =
        countries.joinToString(DB_SEPARATOR) { it.country }

    @TypeConverter
    fun toCountries(stringCountries: String): List<Country> {
        val list: List<Country> = stringCountries.split(DB_SEPARATOR).map { item ->
            object : Country() {
                override val country: String = item
            }
        }
        return list
    }

    companion object {
        private const val DB_SEPARATOR = ","
    }
}