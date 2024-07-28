package com.example.skillcinema.domain.staff

import com.example.skillcinema.data.RepositoryStaff
import com.example.skillcinema.entity.film.Film
import javax.inject.Inject

class GetFilmsFromStaffUseCase @Inject constructor(
    private val repositoryStaff: RepositoryStaff,
) {
    suspend fun execute(staffId: Int): List<Film> = repositoryStaff.getFilmsFromStaff(staffId)
}