package com.example.skillcinema.data.pagging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.skillcinema.data.RepositoryFilms
import com.example.skillcinema.entity.film.Image
import com.example.skillcinema.entity.film.types.GalleryType
import com.example.skillcinema.entity.response.ResponseList
import retrofit2.HttpException

class GalleryPagingSource(
    private val repositoryFilms: RepositoryFilms,
    private val kinopoiskId: Int,
    private val galleryType: GalleryType
) : PagingSource<Int, Image>() {
    override fun getRefreshKey(state: PagingState<Int, Image>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Image> {
        val page = params.key ?: RepositoryFilms.FIRST_PAGE
        val response = try {
            repositoryFilms.getImagesFromFilm(kinopoiskId, galleryType, page)
        } catch (httpException: HttpException) {
            // Иногда сервер отдаёт некорректное общее количество страниц
            // из-за чего приходит ошибка 400, когда запрашиваемая страница вылезает за диапазон.
            // Здесь обрабатывается эта ошибка, чтобы она не вылезала пользователю
            if (httpException.code() == 400)
                ResponseList()
            else
                throw httpException
        }
        val nextPage =
            if (response.totalPages == page || response.items.isEmpty()) null else page + 1
        return LoadResult.Page(response.items, null, nextPage)
    }
}