package com.example.skillcinema.data

import android.content.SharedPreferences
import javax.inject.Inject

class RepositorySettings @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    fun isFirstLaunch(): Boolean {
        val isFirstLaunch = sharedPreferences.getBoolean(IS_FIRST_LAUNCH_TAG, true)
        if (isFirstLaunch) {
            sharedPreferences.edit().putBoolean(IS_FIRST_LAUNCH_TAG, false).apply()
        }
        return isFirstLaunch
    }

    companion object {
        private const val IS_FIRST_LAUNCH_TAG = "is_first_launch"
    }
}