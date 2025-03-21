package com.personal.exo_kit.api.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ActiveVideoMediator {
    private val activeVideoFlow = MutableStateFlow<ActiveVideo>(ActiveVideo.Unknown)
    fun observeActiveVideo(): StateFlow<ActiveVideo> = activeVideoFlow

    fun setActive(mediaId: String) {
        activeVideoFlow.value = ActiveVideo.Known(mediaId)
    }
}

sealed class ActiveVideo {
    data object Unknown : ActiveVideo()
    data class Known(val mediaId: String) : ActiveVideo() {
        override fun toString(): String {
            return mediaId
        }
    }
}