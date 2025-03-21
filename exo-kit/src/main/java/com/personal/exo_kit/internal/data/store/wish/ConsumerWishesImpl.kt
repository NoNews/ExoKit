package com.personal.exo_kit.internal.data.store.wish

import com.personal.exo_kit.internal.MapSharedFlow
import com.personal.exo_kit.internal.mutableSharedMapFlow

class ConsumerWishesImpl : ConsumerWishes {

    private val _consumerWishes = mutableSharedMapFlow<String, ConsumerWish>()
    val consumerWishes: MapSharedFlow<String, ConsumerWish> = _consumerWishes

    override fun wish(mediaId: String, wish: ConsumerWish) {
        _consumerWishes.set(mediaId, wish)
    }
}