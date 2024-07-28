package com.example.skillcinema.domain.previews

import com.example.skillcinema.data.RepositoryFilms
import javax.inject.Inject

// Возвращаем случайный жанр и страну
class GetRandomGenreAndCountryUseCase @Inject constructor(
    private val repositoryFilms: RepositoryFilms
) {
    suspend fun execute(): Pair<String, String> {
        val genre = repositoryFilms.getGenres().random().genre
        val country = repositoryFilms.getCountries().random().country
        return Pair(genre, country)
    }
}