package com.personal.consumer_integration.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.personal.consumer_integration.settings.MediaSessionStore

@Composable
fun rememberMediaSettings(): MediaSessionStore {
    return remember {
        MediaDI.mediaSettingsStore
    }
}