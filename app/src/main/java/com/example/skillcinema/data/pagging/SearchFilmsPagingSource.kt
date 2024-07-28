package com.example.skillcinema.data.pagging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.skillcinema.data.RepositoryFilms
import com.example.skillcinema.entity.film.Film
import com.example.skillcinema.entity.response.ResponseList
import com.example.skillcinema.entity.query.QueryType
import retrofit2.HttpException

class SearchFilmsPagingSource(
    private val repositoryFilms: RepositoryFilms,
    private val searchParams: QueryType.ByParams
) : PagingSource<Int, Film>() {
    override fun getRefreshKey(state: PagingState<Int, Film>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Film> {
        val page = params.key ?: RepositoryFilms.FIRST_PAGE
        val response: ResponseList<Film> = try {
            repositoryFilms.getFilmsByParams(
                searchParams.country,
                searchParams.genre,
                searchParams.order?.name,
                searchParams.typeFilm,
                searchParams.ratingFrom,
                searchParams.ratingTo,
                searchParams.yearFrom,
                searchParams.yearTo,
                searchParams.keyword,
                page
            )
        } catch (httpException: HttpException) {
            // Иногда сервер отдаёт некорректное общее количество страниц
            // из-за чего приходит ошибка 400, когда запрашиваемая страница вылезает за диапазон.
            // Здесь обрабатывается эта ошибка, чтобы она не вылезала пользователю
            if (httpException.code() == 400)
                ResponseList()
            else
                throw httpException
        }

        val filmsByPage = if (searchParams.isShowNotViewedFilms)
            response.items.filter { film -> !film.isViewed }
        else
            response.items
        val nextPage =
            if (response.totalPages == page || response.items.isEmpty()) null else page + 1
        return LoadResult.Page(filmsByPage, null, nextPage)
    }
}