package com.example.skillcinema.data.db.dto.filmState

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "liked_films", indices = [Index(value = ["kinopoisk_id"], unique = true)])
data class LikedFilms(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = null,
    @ColumnInfo(name = "kinopoisk_id")
    val kinopoiskId: Int
)