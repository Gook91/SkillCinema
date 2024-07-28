package com.example.skillcinema.presentation.customViews.horizontalListView.filmList

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinema.R
import com.example.skillcinema.entity.film.Film
import com.example.skillcinema.presentation.lists.holders.FilmCardHolder
import com.example.skillcinema.presentation.lists.adapters.simple.FilmCardAdapter
import com.example.skillcinema.presentation.customViews.horizontalListView.AbstractHorizontalListView

// Горизонтальный вью со списком фильмов и адаптером
class HorizontalFilmListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbstractHorizontalListView<Film, FilmCardHolder, FooterShowAllHolder>(
    context,
    attrs,
    defStyleAttr
) {

    // Задаём высоту прогресс бара, так, чтобы его высота совпадала с высотой будущего списка
    // и элементы на экране не сдвигались после загрузки
    init {
        binding.isLoading.layoutParams.height =
            resources.getDimension(R.dimen.film_card_height).toInt()
    }

    // Объявляем горизонтальный Layout Manager в виде списка
    override val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

    // Возвращаем адаптер с карточкой фильмов
    override fun getMainAdapter(onClickItem: (Int) -> Unit): FilmCardAdapter =
        FilmCardAdapter(onClickItem)

    override fun submitDataInAdapter(dataList: List<Film>) {
        (mainAdapter as FilmCardAdapter).submitList(dataList)
    }

    // Возвращаем футер
    override fun getFooterAdapter(onClickLinkToAll: () -> Unit): RecyclerView.Adapter<FooterShowAllHolder> =
        FooterShowAllAdapter(onClickLinkToAll)
}