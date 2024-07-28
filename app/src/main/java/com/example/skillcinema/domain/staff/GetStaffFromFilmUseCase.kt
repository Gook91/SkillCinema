package com.example.skillcinema.domain.staff

import com.example.skillcinema.data.RepositoryStaff
import com.example.skillcinema.entity.person.Staff
import com.example.skillcinema.entity.person.ProfessionKey
import javax.inject.Inject

class GetStaffFromFilmUseCase @Inject constructor(
    private val repositoryStaff: RepositoryStaff
) {
    suspend fun execute(kinopoiskId: Int, isActors: Boolean): List<Staff> =
        repositoryStaff.getStaffFromFilm(kinopoiskId).filter { person ->
            if (isActors) person.professionKey == ProfessionKey.ACTOR.name
            else person.professionKey != ProfessionKey.ACTOR.name
        }
}