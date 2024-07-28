package com.example.skillcinema.presentation.welcomeActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException
import javax.inject.Inject

class WelcomeViewModelFactory @Inject constructor(
    private val welcomeViewModel: WelcomeViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(WelcomeViewModel::class.java))
            return welcomeViewModel as T
        throw IllegalArgumentException()
    }
}