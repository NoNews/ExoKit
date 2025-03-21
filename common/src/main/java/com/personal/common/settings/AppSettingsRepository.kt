package com.personal.common.settings

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class AppSettingsRepository {

    private val debugEnabled = MutableStateFlow(false)

    fun observeDebugMode(): StateFlow<Boolean> = debugEnabled

    fun toggleDebug() {
        debugEnabled.update {
            !it
        }
    }
}