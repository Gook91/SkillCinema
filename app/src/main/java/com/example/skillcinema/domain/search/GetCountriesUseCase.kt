package com.example.skillcinema.domain.search

import com.example.skillcinema.data.RepositoryFilms
import javax.inject.Inject

class GetCountriesUseCase @Inject constructor(
    private val repositoryFilms: RepositoryFilms
) {
    suspend fun execute(): List<String> = repositoryFilms.getCountries().map { it.country }
}