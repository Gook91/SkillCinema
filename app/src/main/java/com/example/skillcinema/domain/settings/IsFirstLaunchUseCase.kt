package com.example.skillcinema.domain.settings

import com.example.skillcinema.data.RepositorySettings
import javax.inject.Inject

class IsFirstLaunchUseCase @Inject constructor(
    private val repositorySettings: RepositorySettings
) {
    fun execute(): Boolean = repositorySettings.isFirstLaunch()
}