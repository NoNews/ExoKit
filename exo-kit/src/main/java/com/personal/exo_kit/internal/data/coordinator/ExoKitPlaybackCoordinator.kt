package com.personal.exo_kit.internal.data.coordinator

import android.annotation.SuppressLint
import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.source.preload.BasePreloadManager
import androidx.media3.exoplayer.source.preload.DefaultPreloadManager
import androidx.media3.exoplayer.source.preload.PreloadException
import com.personal.exo_kit.api.data.AssociationListener
import com.personal.exo_kit.api.data.ExoKitPlayerPool
import com.personal.exo_kit.internal.data.coordinator.listener.PlayerListener
import com.personal.exo_kit.internal.ui.ExoKitVideoSurface


@SuppressLint("UnsafeOptInUsageError")
class ExoKitPlaybackCoordinator(
    private val context: Context,
    private val playerPool: ExoKitPlayerPool,
    @SuppressLint("UnsafeOptInUsageError") preloadManagerProvider: () -> DefaultPreloadManager
) : BasePreloadManager.Listener {


    private val preloadManager by lazy {
        preloadManagerProvider()
            .apply {
                addListener(this@ExoKitPlaybackCoordinator)
            }

    }

    @SuppressLint("UnsafeOptInUsageError")
    fun prewarm(
        url: String,
        mediaId: String,
        surfaceId: String,
        sessionId: String,
        loop: Boolean,
        position: Int,
    ): Boolean {

        val mediaItem = MediaItem.Builder()
            .setMediaId(mediaId)
            .setUri(url)
            .build()

        preloadManager.add(/* mediaItem = */ mediaItem, /* rankingData = */ position)
        preloadManager.invalidate()

        val player = playerPool.acquire(mediaId)
        println("ExoKit:act:ExoKitPlaybackCoordinator:prewarm, $mediaId, playerName=${player?.name}")


        if (player == null) {
            // player may not be available in the pool if there are adjacent videos in a row
            // in this case, it will be just fetched with preload manager but prepared when playback is active
            return false
        }

        player.prepare(
            mediaId = mediaId,
            surfaceId = sessionId
        ) {
            preloadManager.getMediaSource(mediaItem)!!
        }
        return true
    }

    fun loop(
        mediaId: String,
        surfaceId: String,
        loop: Boolean
    ) {
        val player = playerPool.acquire(mediaId)
        player?.loop(mediaId = mediaId, surfaceId = surfaceId, loop = loop)
    }

    fun play(
        mediaId: String,
        url: String,
        sessionId: String,
        position: Int,
        loop: Boolean,
    ) {
        val player = playerPool.acquire(mediaId)
            ?: error("Impossible to reach here, will be replaced with another code block soon")

        player.play(
            mediaId = mediaId,
            surfaceId = sessionId
        ) {
            val mediaItem = MediaItem.Builder()
                .setMediaId(mediaId)
                .setUri(url)
                .build()
            preloadManager.getMediaSource(mediaItem)!!
        }

        preloadManager.setCurrentPlayingIndex(position)
    }

    fun pause(mediaId: String, surfaceId: String) {
        val player = playerPool.acquire(key = mediaId)
        player?.pause(mediaId = mediaId, surfaceId = surfaceId)
    }

    fun seekTo(mediaId: String, position: Long) {
        val player = playerPool.acquire(key = mediaId)?.exo ?: return
        player.seekTo(position)
    }

    fun seekToFraction(mediaId: String, fraction: Long) {
        val player = playerPool.acquire(key = mediaId)?.exo ?: return
        player.seekTo(fraction)
    }

    fun retry(
        mediaId: String,
        url: String,
        surfaceId: String
    ) {
        val player = playerPool.acquire(key = mediaId) ?: return

        println("ExoKit:act:ExoKitPlaybackCoordinator:retry, $mediaId, playerName=${player?.name}")
        player.retry(mediaId = mediaId, surfaceId = surfaceId, mediaSourceProvider = {
            val mediaItem = MediaItem.Builder()
                .setMediaId(mediaId)
                .setUri(url)
                .build()
            preloadManager.getMediaSource(mediaItem)!!
        })
    }

    fun mute(mediaId: String, mute: Boolean) {
        val player = playerPool.acquire(key = mediaId)?.exo ?: return
        if (mute) {
            player.volume = 0.0f
        } else {
            player.volume = 1.0f
        }
    }


    // BasePreloadManager.Listener methods
    @SuppressLint("UnsafeOptInUsageError")
    override fun onCompleted(mediaItem: MediaItem) {
    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun onError(exception: PreloadException) {
    }

    fun removeSurface(mediaId: String, surfaceId: String) {
        playerPool.cached(key = mediaId)?.clearSurface(surfaceId, mediaId)
    }

    fun registerPlaybackListener(mediaId: String, listener: PlayerListener) {
        val player = playerPool.acquire(key = mediaId)?.exo
        player?.addListener(listener)
    }

    fun unregisterPlaybackListener(mediaId: String, listener: PlayerListener) {
        val player = playerPool.acquire(key = mediaId)?.exo
        player?.removeListener(listener)
    }

    fun setSurface(
        mediaId: String,
        surfaceId: String,
        surface: ExoKitVideoSurface
    ) {
        playerPool.acquire(key = mediaId)?.setSurface(
            surface = surface,
            surfaceId = surfaceId,
            mediaId = mediaId,
        )
    }

    fun registerAssociationListener(listener: AssociationListener) {
        playerPool.addAssociationListener(listener)
    }

    fun unregisterAssociationListener(listener: AssociationListener) {
        playerPool.removeAssociationListener(listener)
    }


}

