package com.example.skillcinema.domain.previews

import com.example.skillcinema.data.RepositoryFilms
import com.example.skillcinema.entity.response.ResponseList
import com.example.skillcinema.entity.film.Film
import com.example.skillcinema.entity.response.TopList
import javax.inject.Inject

class GetPreviewTopListUseCase @Inject constructor(
    private val repositoryFilms: RepositoryFilms
) {
    suspend fun execute(topList: TopList): ResponseList<Film> = repositoryFilms.getTopFilms(topList)
}