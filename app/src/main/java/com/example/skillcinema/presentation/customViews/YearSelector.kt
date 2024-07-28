package com.example.skillcinema.presentation.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.CheckedTextView
import android.widget.TableRow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.setMargins
import com.example.skillcinema.R
import com.example.skillcinema.databinding.ViewYearChangeListBinding

class YearSelector @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    // Начальная инициализация

    private val binding = ViewYearChangeListBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }

    // Переменные годов

    private var firstShowingYear = DEFAULT_FIRST_YEAR
        set(value) {
            field = value
            updateYears(value)
        }
    private val lastShowingYear get() = firstShowingYear + ROWS * COLUMNS - 1

    private fun updateYears(startYear: Int) {
        var yearToSet = startYear
        yearViewList.forEach { yearView ->
            yearView.isChecked = yearToSet == selectedYear
            yearView.text = yearToSet++.toString()
        }
        binding.rangeYears.text =
            context.getString(R.string.years_with_separator, firstShowingYear, lastShowingYear)
    }

    var selectedYear: Int? = null
        set(value) {
            field = value
            var step: Int = value?.let { (value - firstShowingYear) / (ROWS * COLUMNS) } ?: return
            if (value < firstShowingYear) step--
            firstShowingYear += step * ROWS * COLUMNS
        }

    // Настройка таблицы годов

    private val yearViewList = mutableListOf<CheckedTextView>()

    private val onYearClickListener = OnClickListener { view ->
        if (view !is CheckedTextView) return@OnClickListener
        val clickedYear: Int = view.text.toString().toIntOrNull() ?: return@OnClickListener
        selectedYear = if (clickedYear == selectedYear) null else clickedYear
    }

    init {
        for (i in 0 until ROWS) {
            val tableRow = TableRow(context)

            for (j in 0 until COLUMNS) {
                val textView = CheckedTextView(context, attrs, defStyleAttr, R.style.YearTextView)
                textView.setOnClickListener(onYearClickListener)

                val marginLayoutParams = TableRow.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    resources.getDimensionPixelSize(R.dimen.year_text_view_height),
                    1f
                )
                marginLayoutParams.setMargins(resources.getDimensionPixelSize(R.dimen.small_borders))
                textView.layoutParams = marginLayoutParams

                tableRow.addView(textView)
                yearViewList.add(textView)
            }
            binding.yearsTable.addView(tableRow, i)
        }
        updateYears(firstShowingYear)
    }

    // Кнопки перелистывания списка годов

    init {
        binding.previousYears.setOnClickListener {
            firstShowingYear -= ROWS * COLUMNS
        }
        binding.nextYears.setOnClickListener {
            firstShowingYear += ROWS * COLUMNS
        }
    }

    companion object {
        private const val ROWS = 4
        private const val COLUMNS = 3
        private const val DEFAULT_FIRST_YEAR = 2023
    }
}