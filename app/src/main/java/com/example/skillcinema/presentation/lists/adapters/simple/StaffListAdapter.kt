package com.example.skillcinema.presentation.lists.adapters.simple

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.example.skillcinema.databinding.ItemStaffBinding
import com.example.skillcinema.entity.person.Staff
import com.example.skillcinema.presentation.lists.holders.StaffHolder

// Адаптер для горизонтального списка прокрутки с участниками фильма
class StaffListAdapter(
    private val onClickItem: (staffId: Int) -> Unit // Слушатель щечка по элементу
) : Adapter<StaffHolder>() {

    private var staffList: List<Staff> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(dataList: List<Staff>) {
        staffList = dataList
        notifyDataSetChanged()
    }

    // Создаём элемент списка
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaffHolder {
        val binding = ItemStaffBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return StaffHolder(binding)
    }

    // Заполняем элемент списка
    override fun onBindViewHolder(holder: StaffHolder, position: Int) {
        // Получаем данные о человеке и заполняем их
        val staff = staffList[position]
        with(holder.binding) {
            name.text = if (staff.nameRu.isNullOrBlank()) staff.nameEn else staff.nameRu
            // Если нет описания роли, то ставим профессию
            description.text = staff.description ?: staff.profession
            Glide.with(holder.itemView.context)
                .load(staff.posterUrl)
                .into(photo)
            root.setOnClickListener { onClickItem(staff.personId) }
        }
    }

    override fun getItemCount(): Int = staffList.size
}