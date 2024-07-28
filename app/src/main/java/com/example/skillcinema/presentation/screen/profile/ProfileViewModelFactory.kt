package com.example.skillcinema.presentation.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException
import javax.inject.Inject

class ProfileViewModelFactory @Inject constructor(
    private val profileViewModel: ProfileViewModel
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java))
            return profileViewModel as T
        throw IllegalArgumentException()
    }
}