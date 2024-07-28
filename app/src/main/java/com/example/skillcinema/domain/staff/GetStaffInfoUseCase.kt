package com.example.skillcinema.domain.staff

import com.example.skillcinema.data.RepositoryStaff
import com.example.skillcinema.entity.person.Staff
import javax.inject.Inject

class GetStaffInfoUseCase @Inject constructor(
    private val repositoryStaff: RepositoryStaff
) {
    suspend fun execute(staffId: Int): Staff = repositoryStaff.getStaffInfo(staffId)
}