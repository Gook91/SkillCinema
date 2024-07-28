package com.example.skillcinema.data.db.dto.collections

import androidx.room.ColumnInfo
import com.example.skillcinema.entity.collections.CollectionFilms
import com.example.skillcinema.entity.collections.TypeCollection

data class CollectionWithCountFilmsDto(
    @ColumnInfo(name = "collection_id")
    val collectionId: Int,
    @ColumnInfo(name = "collection_name")
    val collectionName: String,
    @ColumnInfo(name = "count_films")
    val countFilms: Int
) {
    fun toUserCollection(): CollectionFilms =
        CollectionFilms(
            type = TypeCollection.UserCollection(collectionId, collectionName),
            countFilms = countFilms
        )
}