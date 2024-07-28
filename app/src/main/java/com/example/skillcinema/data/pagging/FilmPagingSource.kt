package com.example.skillcinema.data.pagging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.skillcinema.data.RepositoryFilms
import com.example.skillcinema.entity.response.ResponseList
import com.example.skillcinema.entity.film.Film
import com.example.skillcinema.entity.query.QueryType
import retrofit2.HttpException

class FilmPagingSource(
    private val repositoryFilms: RepositoryFilms,
    private val queryType: QueryType,
) : PagingSource<Int, Film>() {
    override fun getRefreshKey(state: PagingState<Int, Film>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Film> {
        val page = params.key ?: RepositoryFilms.FIRST_PAGE
        val response: ResponseList<Film> = try {
            when (queryType) {
                is QueryType.Top -> repositoryFilms.getTopFilms(queryType.type, page)
                is QueryType.ByParams -> repositoryFilms.getFilmsByParams(
                    country = queryType.country,
                    genre = queryType.genre,
                    typeFilm = queryType.typeFilm,
                )

                is QueryType.Premieres -> repositoryFilms.getPremiereFilms(page)
                is QueryType.Similars -> repositoryFilms.getSimilarFilms(queryType.id, page)
            }
        } catch (httpException: HttpException) {
            // Иногда сервер отдаёт некорректное общее количество страниц
            // из-за чего приходит ошибка 400, когда запрашиваемая страница вылезает за диапазон.
            // Здесь обрабатывается эта ошибка, чтобы она не вылезала пользователю
            if (httpException.code() == 400)
                ResponseList()
            else
                throw httpException
        }
        val filmsByPage: List<Film> = response.items
        val nextPage =
            if (response.totalPages == page || response.items.isEmpty()) null else page + 1
        return LoadResult.Page(filmsByPage, null, nextPage)
    }
}