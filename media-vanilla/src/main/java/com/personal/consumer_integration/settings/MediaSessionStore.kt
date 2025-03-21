package com.personal.consumer_integration.settings

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MediaSessionStore {

    private val muteFlow = MutableStateFlow(true)
    private val autoplayFlow = MutableStateFlow(true)

    fun sessionMuted():Boolean = muteFlow.value

    fun observeMuteSession(): StateFlow<Boolean> = muteFlow

    fun observeAutoplaySession(): StateFlow<Boolean> = autoplayFlow

    fun setAutoplay(autoplay: Boolean) {
        autoplayFlow.value = autoplay
    }

    fun setMuted(muted: Boolean) {
        muteFlow.value = muted
    }
}