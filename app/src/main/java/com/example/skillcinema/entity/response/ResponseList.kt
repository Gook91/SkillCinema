package com.example.skillcinema.entity.response

// Абстрактный список ответа
open class ResponseList<out T>(
    open val total: Int? = null, // Всего объектов
    open val totalPages: Int = 1, // Всего страниц
    open val items: List<T> = emptyList() // Список объектов
)