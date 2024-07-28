package com.example.skillcinema.entity.collections

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class TypeCollection : Parcelable {

    object Liked : TypeCollection()
    object WatchLater : TypeCollection()
    object Viewed : TypeCollection()
    object Interested : TypeCollection()

    data class UserCollection(
        val id: Int,
        val name: String
    ) : TypeCollection()
}
