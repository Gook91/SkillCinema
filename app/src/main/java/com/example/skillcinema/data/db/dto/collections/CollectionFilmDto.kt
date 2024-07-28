package com.example.skillcinema.data.db.dto.collections

import androidx.room.ColumnInfo
import androidx.room.Entity

// Таблица связи фильмов и коллекция

@Entity(
    tableName = "collection_film",
    primaryKeys = ["collection_id", "kinopoisk_id"]
)
data class CollectionFilmDto(
    @ColumnInfo(name = "collection_id")
    val collectionId: Int,
    @ColumnInfo(name = "kinopoisk_id")
    val kinopoiskId: Int
)