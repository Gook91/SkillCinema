package com.example.skillcinema.data

import com.example.skillcinema.data.db.StaffDao
import com.example.skillcinema.data.db.dto.staff.StaffLocalDto
import com.example.skillcinema.data.network.StaffApi
import com.example.skillcinema.entity.film.Film
import com.example.skillcinema.entity.person.Staff
import javax.inject.Inject

class RepositoryStaff @Inject constructor(
    private val staffApi: StaffApi,
    private val staffDao: StaffDao,
    private val memoryStorage: MemoryStorage
) {

    // Возвращаем персонал, участвующий в фильме
    suspend fun getStaffFromFilm(kinopoiskId: Int): List<Staff> {
        if (!memoryStorage.staffFromFilm[kinopoiskId].isNullOrEmpty()) {
            return memoryStorage.staffFromFilm[kinopoiskId]!!
        }
        val response = staffApi.getPersonsFromFilm(kinopoiskId)
        return if (response.isSuccessful && response.body() != null) {
            memoryStorage.staffFromFilm[kinopoiskId] = response.body()!!
            response.body()!!
        } else
            emptyList()
    }

    suspend fun getStaffInfo(staffId: Int): Staff =
        memoryStorage.staff.getOrPut(staffId) {
            staffDao.getStaff(staffId)?.toStaff() ?: staffApi.getStaffInfo(staffId).also { staff ->
                staffDao.add(StaffLocalDto(staff))
            }
        }

    /** Функция фильмов по актёру не хранит данные о фильмах в базе данных, так как это потребует
     * либо запроса каждого недостающего фильма с сервера (примерно 50-100 запросов на актёра),
     * либо создания промежуточной базы с неполной информацией о фильмах.
     * При это эта информация будет быстро стареть, так как изменение списка фильмов локально не будет видно
     **/
    suspend fun getFilmsFromStaff(staffId: Int): List<Film> =
        memoryStorage.staff.getOrPut(staffId) {
            staffApi.getStaffInfo(staffId)
        }.films ?: emptyList()
}