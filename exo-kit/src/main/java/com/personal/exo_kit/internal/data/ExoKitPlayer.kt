package com.personal.exo_kit.internal.data

import android.annotation.SuppressLint
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import com.personal.exo_kit.internal.ui.ExoKitVideoSurface

class ExoKitPlayer(val exo: ExoPlayer, val name: PlayerName) {

    @Volatile
    private var owningSurfaceId: String = ABSENT

    fun isDirty(): Boolean = owningSurfaceId != ABSENT

    fun setSurface(surface: ExoKitVideoSurface, surfaceId: String, mediaId: String) {
        println("ExoKit:act:ExoKitPlayer:setSurface, $mediaId#$surfaceId, owningId: $owningSurfaceId, player: $name")
        if (!isDirty()) {
            exo.setVideoTextureView(surface)
            owningSurfaceId = surfaceId
            println("ExoKit:act:ExoKitPlayer:setSurface not dirty, and was set. New owner: $owningSurfaceId, player: $name")
        }
    }

    fun clearSurface(surfaceId: String, mediaId: String) {
        println("ExoKit:act:ExoKitPlayer:clearSurface, $mediaId#$surfaceId, current owningId: $owningSurfaceId, player: $name")
        if (owningSurfaceId != surfaceId) {
            println("ExoKit:act:ExoKitPlayer:clearSurface, $mediaId#$surfaceId returned without clearing, player: $name")
            return
        }
        if (exo.isPlaying || exo.playWhenReady) {
            // case: video is preparing but scrolled fast and paused, so in this case, it won't be yet playing but playWhenReady will be set to true
            println("ExoKit:act:ExoKitPlayer:clearSurface, was playing or about to be, paused, player: $name")
            exo.playWhenReady = false
        }
        exo.clearVideoSurface()
        owningSurfaceId = ABSENT
        println("ExoKit:act:ExoKitPlayer:clearSurface, $mediaId#$surfaceId completed, player: $name")
    }

    @SuppressLint("UnsafeOptInUsageError")
    fun prepare(
        mediaId: String,
        surfaceId: String,
        mediaSourceProvider: () -> MediaSource,
    ) {
        if (exo.playbackState == Player.STATE_IDLE) {
            val mediaSource = mediaSourceProvider.invoke()
            exo.setMediaSource(mediaSource)
            exo.prepare()
            println("ExoKit:act:ExoKitPlayer:prewarm, $mediaId#$surfaceId, player: ${name}, was IDLE, now prepared!")
            return
        }
        println("ExoKit:act:ExoKitPlayer:prewarm, $mediaId#$surfaceId, player: ${name}, is already prewarmed, no action is required")
    }

    fun pause(mediaId: String, surfaceId: String) {
        if (exo.isPlaying || exo.playWhenReady) {
            // we need to check playWhenReady because video may be still in "loading" phase and in fact not be played yet.
            exo.playWhenReady = false
        }
    }

    fun loop(mediaId: String, surfaceId: String, loop: Boolean) {
        exo.repeatMode = if (loop) {
            Player.REPEAT_MODE_ALL
        } else {
            Player.REPEAT_MODE_OFF
        }
    }

    fun getBufferedPosition(mediaId: String, surfaceId: String): Long {
        return exo.bufferedPosition
    }

    fun replay(
        mediaId: String,
        surfaceId: String,
        mediaSourceProvider: () -> MediaSource,
    ) {
        println("ExoKit:act:ExoKitPlayer:replay, $mediaId#$surfaceId, player:${name}, state: ${exo.playbackState}")
        val playbackState = exo.playbackState
        if (playbackState == Player.STATE_IDLE) {
            println("ExoKit:act:ExoKitPlayer:replay, $mediaId#$surfaceId, player:${name} was IDLE, preparing...")
            prepare(
                mediaId = mediaId,
                surfaceId = surfaceId,
                mediaSourceProvider = mediaSourceProvider,
            )
        }

        if (playbackState == Player.STATE_ENDED) {
            exo.seekToDefaultPosition()
        }

        if (!exo.playWhenReady) {
            exo.playWhenReady = true
            println("ExoKit:act:ExoKitPlayer:replay, $mediaId#$surfaceId, player:${name} playWhenReady was false, now true")
        }
    }

    fun play(
        mediaId: String,
        surfaceId: String,
        mediaSourceProvider: () -> MediaSource,
    ) {
        println("ExoKit:act:ExoKitPlayer:play, $mediaId#$surfaceId, player:${name}, state: ${exo.playbackState}")
        val playbackState = exo.playbackState
        if (playbackState == Player.STATE_IDLE) {
            println("ExoKit:act:ExoKitPlayer:play, $mediaId#$surfaceId, player:${name} was IDLE, preparing...")
            prepare(
                mediaId = mediaId,
                surfaceId = surfaceId,
                mediaSourceProvider = mediaSourceProvider,
            )
        }

        if (!exo.playWhenReady) {
            exo.playWhenReady = true
            println("ExoKit:act:ExoKitPlayer:play, $mediaId#$surfaceId, player:${name} playWhenReady was false, now true")
        }
    }

    fun retry(
        mediaId: String,
        surfaceId: String,
        mediaSourceProvider: () -> MediaSource,

        ) {
        prepare(mediaId = mediaId, surfaceId = surfaceId, mediaSourceProvider = mediaSourceProvider)
        play(mediaId = mediaId, surfaceId = surfaceId, mediaSourceProvider = mediaSourceProvider)
    }

    companion object {
        const val ABSENT = "absent"
    }
}

enum class PlayerName {
    DYLAN,
    IRENE,
    BIDIT,
}