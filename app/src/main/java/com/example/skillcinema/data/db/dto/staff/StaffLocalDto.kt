package com.example.skillcinema.data.db.dto.staff

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.skillcinema.entity.person.Staff

@Entity(tableName = "staff")
data class StaffLocalDto(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "person_id")
    val personId: Int,
    @ColumnInfo(name = "name_ru")
    val nameRu: String? = null,
    @ColumnInfo(name = "name_en")
    val nameEn: String? = null,
    @ColumnInfo(name = "poster_url")
    val posterUrl: String,
    @ColumnInfo(name = "profession")
    val profession: String? = null,
) {
    constructor(staff: Staff) : this(
        personId = staff.personId,
        nameRu = staff.nameRu,
        nameEn = staff.nameEn,
        posterUrl = staff.posterUrl,
        profession = staff.profession
    )

    fun toStaff(): Staff {
        return object : Staff() {
            override val personId = this@StaffLocalDto.personId
            override val nameRu = this@StaffLocalDto.nameRu
            override val nameEn = this@StaffLocalDto.nameEn
            override val posterUrl = this@StaffLocalDto.posterUrl
            override val profession = this@StaffLocalDto.profession
        }
    }
}