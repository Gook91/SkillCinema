package com.example.skillcinema.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.skillcinema.data.db.dto.staff.StaffLocalDto

@Dao
interface StaffDao {
    @Query("SELECT * FROM staff WHERE person_id=:staffId")
    suspend fun getStaff(staffId: Int): StaffLocalDto?

    @Insert
    suspend fun add(staff: StaffLocalDto)
}