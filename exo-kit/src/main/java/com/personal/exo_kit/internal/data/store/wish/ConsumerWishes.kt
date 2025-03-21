package com.personal.exo_kit.internal.data.store.wish

interface ConsumerWishes {
    fun wish(mediaId: String, wish: ConsumerWish)
}

sealed class ConsumerWish {
    data object Play : ConsumerWish()
    data object Pause : ConsumerWish()
    data class SeekFraction(val fraction: Float) : ConsumerWish()
    data class SeekPosition(val position: Long) : ConsumerWish()
    data class PriorityChanged(
        val fraction: Float,
    ) : ConsumerWish()

    data object Retry : ConsumerWish()

    data class ToggleMute(val mute: Boolean) : ConsumerWish()
}
