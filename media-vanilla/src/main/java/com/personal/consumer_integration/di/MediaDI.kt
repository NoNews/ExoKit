package com.personal.consumer_integration.di

import com.personal.consumer_integration.settings.MediaSessionStore

object MediaDI {
    val mediaSettingsStore by lazy {
        MediaSessionStore()
    }
}