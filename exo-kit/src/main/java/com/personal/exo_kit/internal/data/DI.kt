package com.personal.exo_kit.internal.data

import com.personal.exo_kit.api.data.ActiveVideoMediator
import com.personal.exo_kit.internal.data.coordinator.ExoKitPlaybackCoordinator
import com.personal.exo_kit.internal.data.coordinator.ExoKitPriorityCoordinator
import com.personal.exo_kit.internal.data.store.ExoKitPlaybackStoreImpl
import com.personal.exo_kit.internal.data.store.wish.ExoKitWishes
import com.personal.exo_kit.internal.data.store.wish.ExoKitWishesImpl

internal object DI {
    private lateinit var visibilityCoordinatorProvider: () -> ExoKitPriorityCoordinator
    private lateinit var playbackCoordinatorProvider: () -> ExoKitPlaybackCoordinator
    private lateinit var playbackStoreProvider: () -> ExoKitPlaybackStoreImpl
    private lateinit var consumerWishesProvider: () -> ExoKitWishes
    private lateinit var poolProvider: () -> ExoKitPlaybackStoreImpl

    val visibilityCoordinator by lazy {
        visibilityCoordinatorProvider()
    }
    val playbackCoordinator by lazy {
        playbackCoordinatorProvider()
    }

    val consumerWishes by lazy {
        consumerWishesProvider()
    }
    val playbackStore by lazy {
        playbackStoreProvider()
    }

    val playerPool by lazy {
        playbackStoreProvider()
    }

    val activeVideoMediator by lazy {
        ActiveVideoMediator()
    }

    internal fun setExoKitPlaybackCoordinator(provider: () -> ExoKitPlaybackCoordinator) {
        playbackCoordinatorProvider = provider
    }

    internal fun setExoKitVisibilityCoordinator(provider: () -> ExoKitPriorityCoordinator) {
        visibilityCoordinatorProvider = provider
    }

    fun setPlaybackStore(function: () -> ExoKitPlaybackStoreImpl) {
        playbackStoreProvider = function
    }

    fun setWishes(function: () -> ExoKitWishesImpl) {
        consumerWishesProvider = function
    }


}