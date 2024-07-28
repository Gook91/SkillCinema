package com.example.skillcinema.presentation.customViews.horizontalListView

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.skillcinema.R
import com.example.skillcinema.entity.person.Staff
import com.example.skillcinema.presentation.lists.holders.StaffHolder
import com.example.skillcinema.presentation.lists.adapters.simple.StaffListAdapter

// Горизонтальный вью с персоналом фильма
class HorizontalStaffListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbstractHorizontalListView<Staff, StaffHolder, ViewHolder>(
    context,
    attrs,
    defStyleAttr
) {
    // Создаём переменную с количеством строк
    private var spanCount = DEFAULT_SPAN_COUNT

    init {
        // Получаем из xml количество строк
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.HorizontalStaffListView,
            0, 0
        ).apply {
            try {
                spanCount =
                    getInt(R.styleable.HorizontalStaffListView_spanCount, DEFAULT_SPAN_COUNT)
            } finally {
                recycle()
            }
        }

        // Задаём высоту прогресс бара, так, чтобы его высота совпадала с высотой будущего списка
        // и элементы на экране не сдвигались после загрузки
        binding.isLoading.layoutParams.height =
            resources.getDimension(R.dimen.staff_height).toInt() * spanCount
    }

    // Объявляем отложенный горизонтальный Layout Manager в виде таблицы.
    // Отложенное создание необходимо для назначения количества строк, после получения его из xml
    override val layoutManager: RecyclerView.LayoutManager by lazy {
        GridLayoutManager(context, spanCount, GridLayoutManager.HORIZONTAL, false)
    }

    // Возвращаем адаптер с персоналом
    override fun getMainAdapter(
        onClickItem: (Int) -> Unit
    ): StaffListAdapter = StaffListAdapter(onClickItem)

    override fun submitDataInAdapter(dataList: List<Staff>) {
        (mainAdapter as StaffListAdapter).submitData(dataList)
    }

    // Возвращаем null, так как футер не нужен
    override fun getFooterAdapter(onClickLinkToAll: () -> Unit): RecyclerView.Adapter<ViewHolder>? =
        null

    companion object {
        // Количество строк по умолчанию
        private const val DEFAULT_SPAN_COUNT = 2
    }
}