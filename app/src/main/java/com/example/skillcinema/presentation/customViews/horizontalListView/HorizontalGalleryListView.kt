package com.example.skillcinema.presentation.customViews.horizontalListView

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.skillcinema.R
import com.example.skillcinema.entity.film.Image
import com.example.skillcinema.presentation.lists.holders.GalleryHolder
import com.example.skillcinema.presentation.lists.adapters.simple.GalleryListAdapter

// Горизонтальный вью с изображениями из галереи
class HorizontalGalleryListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbstractHorizontalListView<Image, GalleryHolder, ViewHolder>(
    context,
    attrs,
    defStyleAttr
) {
    // Задаём высоту прогресс бара, так, чтобы его высота совпадала с высотой будущего списка
    // и элементы на экране не сдвигались после загрузки
    init {
        binding.isLoading.layoutParams.height =
            resources.getDimension(R.dimen.gallery_height).toInt()
    }

    // Объявляем горизонтальный Layout Manager в виде списка
    override val layoutManager: RecyclerView.LayoutManager =
        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

    // Возвращаем адаптер с картинками
    override fun getMainAdapter(
        onClickItem: (Int) -> Unit
    ): GalleryListAdapter = GalleryListAdapter()

    override fun submitDataInAdapter(dataList: List<Image>) {
        (mainAdapter as GalleryListAdapter).submitList(dataList)
    }

    // Возвращаем null, так как футер не нужен
    override fun getFooterAdapter(onClickLinkToAll: () -> Unit): RecyclerView.Adapter<ViewHolder>? =
        null
}