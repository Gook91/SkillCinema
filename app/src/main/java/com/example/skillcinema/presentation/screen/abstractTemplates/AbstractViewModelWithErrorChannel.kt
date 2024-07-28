package com.example.skillcinema.presentation.screen.abstractTemplates

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

abstract class AbstractViewModelWithErrorChannel : ViewModel() {

    // Инициализируем канал ошибок
    private val _isErrorChannel = Channel<Boolean>()
    val isErrorChannel = _isErrorChannel.receiveAsFlow()


    // Создаём обработчик ошибок для корутины
    private val queryExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(LOG_TAG, "Error in query :$throwable", throwable)

        FirebaseCrashlytics.getInstance().recordException(throwable)

        viewModelScope.launch {
            _isErrorChannel.send(true)
        }
    }

    protected val viewModelScopeWithExceptionHandler =
        CoroutineScope(viewModelScope.coroutineContext + queryExceptionHandler)

    companion object {
        private const val LOG_TAG = "Intercepted errors"
    }
}