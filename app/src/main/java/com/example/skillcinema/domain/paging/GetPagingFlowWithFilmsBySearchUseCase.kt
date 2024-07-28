package com.example.skillcinema.domain.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.skillcinema.data.RepositoryFilms
import com.example.skillcinema.data.pagging.SearchFilmsPagingSource
import com.example.skillcinema.entity.film.Film
import com.example.skillcinema.entity.query.QueryType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPagingFlowWithFilmsBySearchUseCase @Inject constructor(
    private val repositoryFilms: RepositoryFilms
) {
    fun execute(searchParams: QueryType.ByParams): Flow<PagingData<Film>> {
        val factory: (QueryType.ByParams) -> SearchFilmsPagingSource = { params ->
            SearchFilmsPagingSource(repositoryFilms, params)
        }
        return Pager(
            PagingConfig(20)
        ) {
            factory(searchParams)
        }.flow
    }
}