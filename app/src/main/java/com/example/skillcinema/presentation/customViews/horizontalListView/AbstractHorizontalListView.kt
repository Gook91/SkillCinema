package com.example.skillcinema.presentation.customViews.horizontalListView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.skillcinema.R
import com.example.skillcinema.databinding.ViewHorizontalListBinding

// Абстрактный класс списка предварительного просмотра с заголовком и ссылками на полный список элементов
abstract class AbstractHorizontalListView<T, MVH : ViewHolder, FVH : ViewHolder>
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    // Binding для вью
    protected val binding = ViewHorizontalListBinding.inflate(LayoutInflater.from(context))

    // Блокировка перезаписи функции добавления вью
    final override fun addView(child: View?, width: Int, height: Int) =
        super.addView(child, width, height)

    init {
        // Получаем заголовок из xml
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.AbstractHorizontalListView,
            0, 0
        ).apply {
            try {
                // Если он есть, то устанавливаем его
                setTitle(getString(R.styleable.AbstractHorizontalListView_title_text).toString())
            } finally {
                // Освобождаем массив
                recycle()
            }
        }
        addView(
            binding.root,
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
    }

    fun setTitle(title: String) {
        binding.title.text = title
    }

    fun setCountOnLinkToAll(countAllItems: Int?) {
        binding.goToAllLink.text = if (countAllItems == null)
            context.getString(R.string.all)
        else
            context.getString(R.string.all_with_count, countAllItems)
    }

    // Layout manager для списка
    protected abstract val layoutManager: LayoutManager

    var mainAdapter: Adapter<MVH>? = null
    protected abstract fun getMainAdapter(onClickItem: (Int) -> Unit): Adapter<MVH>

    // Возвращаем футер если есть или null
    protected abstract fun getFooterAdapter(onClickLinkToAll: (() -> Unit)): Adapter<FVH>?

    private var isDataNotSubmitEarlier = true
    fun submitData(dataList: List<T>) {
        if (isDataNotSubmitEarlier) {
            binding.dataList.visibility = View.VISIBLE
            binding.isLoading.visibility = View.GONE
            isDataNotSubmitEarlier = false
        }
        submitDataInAdapter(dataList)
    }

    protected abstract fun submitDataInAdapter(dataList: List<T>)

    fun setAndShowData(
        dataList: List<T>,
        onClickItem: (Int) -> Unit,
        onClickLinkToAll: (() -> Unit)? = null,
        countAllItems: Int? = null
    ) {
        ListWithLinkBuilder(onClickItem)
            .setOnClickLinkToAll(onClickLinkToAll)
            .setCountItems(countAllItems)
            .setClickFooterSameAsLinkToAll()
            .build()
        submitData(dataList)
    }

    var isAdaptersSet = false
        private set

    inner class ListWithLinkBuilder(_onClickItem: (Int) -> Unit) {
        private val onClickItem: ((Int) -> Unit) = _onClickItem
        private var onClickLinkToAll: (() -> Unit)? = null
        private var countAllItems: Int? = null
        private var onClickFooter: (() -> Unit)? = null
        private var isFooterSameAsLink: Boolean = false

        fun setOnClickLinkToAll(_onClickLinkToAll: (() -> Unit)?): ListWithLinkBuilder {
            onClickLinkToAll = _onClickLinkToAll
            return this
        }

        fun setCountItems(_countAllItems: Int?): ListWithLinkBuilder {
            countAllItems = _countAllItems
            return this
        }

        fun setOnClickFooter(_onClickFooter: (() -> Unit)?): ListWithLinkBuilder {
            onClickFooter = _onClickFooter
            return this
        }

        fun setClickFooterSameAsLinkToAll(isSame: Boolean = true): ListWithLinkBuilder {
            isFooterSameAsLink = isSame
            return this
        }

        fun build() {
            binding.dataList.layoutManager = layoutManager

            mainAdapter = getMainAdapter(onClickItem)
            val loadedAdapters = ConcatAdapter(mainAdapter)

            onClickLinkToAll?.let { onClick ->
                binding.goToAllLink.visibility = View.VISIBLE
                binding.goToAllLink.setOnClickListener { onClick() }
                setCountOnLinkToAll(countAllItems)
            }

            val onClickFooterFinal =
                (if (isFooterSameAsLink) onClickLinkToAll else onClickFooter)
            onClickFooterFinal?.let {
                getFooterAdapter(it)?.let { footerAdapter ->
                    loadedAdapters.addAdapter(footerAdapter)
                }
            }

            binding.dataList.adapter = loadedAdapters
            isAdaptersSet = true
        }
    }
}