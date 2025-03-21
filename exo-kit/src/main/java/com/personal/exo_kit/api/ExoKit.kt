package com.personal.exo_kit.api

import android.annotation.SuppressLint
import android.content.Context
import androidx.media3.exoplayer.source.preload.DefaultPreloadManager
import com.personal.exo_kit.api.data.ExoKitPlayerPool
import com.personal.exo_kit.internal.data.DI
import com.personal.exo_kit.internal.data.coordinator.ExoKitPlaybackCoordinator
import com.personal.exo_kit.internal.data.coordinator.ExoKitPriorityCoordinator
import com.personal.exo_kit.internal.data.store.ExoKitPlaybackStoreImpl
import com.personal.exo_kit.internal.data.store.wish.ExoKitWishesImpl

object ExoKit {

    fun initialise(
        context: Context,
        poolProvider: () -> ExoKitPlayerPool,
        @SuppressLint("UnsafeOptInUsageError") preloadManagerProvider: () -> DefaultPreloadManager
    ) {

        val pool = poolProvider.invoke()
        DI.setExoKitPlaybackCoordinator {
            ExoKitPlaybackCoordinator(
                context = context,
                playerPool = pool,
                preloadManagerProvider = preloadManagerProvider,
            )
        }

        DI.setExoKitVisibilityCoordinator {
            ExoKitPriorityCoordinator()
        }

        DI.setWishes { ExoKitWishesImpl() }
        DI.setPlaybackStore {
            ExoKitPlaybackStoreImpl(
            )
        }
    }
}