package com.example.skillcinema.domain.paging

import com.example.skillcinema.data.RepositoryFilms
import com.example.skillcinema.data.pagging.FilmPagingSource
import com.example.skillcinema.entity.query.QueryType
import javax.inject.Inject

class GetPagingFilmsUseCase @Inject constructor(
    private val repositoryFilms: RepositoryFilms
) {
    fun execute(queryType: QueryType): FilmPagingSource {
        val factory: (QueryType) -> FilmPagingSource = { query ->
            FilmPagingSource(repositoryFilms, query)
        }
        return factory(queryType)
    }
}