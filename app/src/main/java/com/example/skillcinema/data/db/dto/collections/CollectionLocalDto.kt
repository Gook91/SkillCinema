package com.example.skillcinema.data.db.dto.collections

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collections")
data class CollectionLocalDto(
    @PrimaryKey @ColumnInfo(name = "collection_id")
    val collectionId: Int? = null,
    @ColumnInfo(name = "collection_name")
    val collectionName: String
){
    constructor(name: String) : this(collectionName = name)
}
