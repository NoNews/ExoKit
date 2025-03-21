package com.personal.exo_kit.internal.data.coordinator.listener

import android.annotation.SuppressLint
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import com.personal.exo_kit.internal.data.exo.captionsAvailable
import com.personal.exo_kit.internal.data.exo.getTracksInfo
import com.personal.exo_kit.internal.data.exo.soundAvailable

open class PlayerListener : Player.Listener {

    private var lastEvent: PlayerEvent? = null

    @SuppressLint("UnsafeOptInUsageError")
    override fun onEvents(player: Player, events: Player.Events) {
        super.onEvents(player, events)

        if (events.contains(Player.EVENT_TIMELINE_CHANGED)) {
            println("PlayerListener: StateChanged, processTimelineChanged")
            processTimelineChanged(player)
        }
        if (events.contains(Player.EVENT_MEDIA_ITEM_TRANSITION)) {
            println("PlayerListener: StateChanged, mediaItemTransition")
            processMediaItemTransition(player)
        }
        if (events.contains(Player.EVENT_TRACKS_CHANGED)) {
            println("PlayerListener: StateChanged, tracksChanged")
            processTracksChanged(player)
        }
        if (events.contains(Player.EVENT_SURFACE_SIZE_CHANGED)) {
            onPlayerEvent(
                PlayerEvent.SurfaceSizeChanged(
                    width = player.surfaceSize.width,
                    height = player.surfaceSize.height
                )
            )
        }

        if (events.contains(Player.EVENT_RENDERED_FIRST_FRAME)) {
            onPlayerEvent(PlayerEvent.RenderedFirstFrame)
        }

        if (events.contains(Player.EVENT_VIDEO_SIZE_CHANGED)) {
            println("PlayerListener: StateChanged, videoSizeChanged")
            processVideoSizeChanged(player)
        }

        if (events.contains(Player.EVENT_PLAYBACK_STATE_CHANGED)
            || events.contains(Player.EVENT_PLAY_WHEN_READY_CHANGED)
            || events.contains(Player.EVENT_IS_PLAYING_CHANGED)
        ) {
            processPlaybackStateChanged(player)
        }
        if (events.contains(Player.EVENT_PLAY_WHEN_READY_CHANGED)) {
            println("PlayerListener: StateChanged, play when ready changed, ${player.playWhenReady}")
            processPlayWhenReadyChanged(player)
        }

        if (events.contains(Player.EVENT_PLAYER_ERROR)) {
            println("PlayerListener: StateChanged, playerError, ${player.isPlaying}")
            processPlayerError(player.playerError)
        }
    }

    override fun onPositionDiscontinuity(
        oldPosition: Player.PositionInfo,
        newPosition: Player.PositionInfo,
        reason: Int
    ) {
        super.onPositionDiscontinuity(oldPosition, newPosition, reason)

        if (reason == Player.DISCONTINUITY_REASON_AUTO_TRANSITION) {
            onPlayerEvent(PlayerEvent.VideoLooped)
        }
    }

    private fun processVideoSizeChanged(player: Player) {
        onPlayerEvent(
            PlayerEvent.VideoSizeChanged(
                width = player.videoSize.width,
                height = player.videoSize.height
            )
        )
    }


    private fun processTimelineChanged(player: Player) {
    }

    private fun processMediaItemTransition(player: Player) {
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun processTracksChanged(player: Player) {
        val tracks = player.getTracksInfo()
        onPlayerEvent(
            PlayerEvent.TracksChanged(
                tracks = tracks,
                hasSound = player.soundAvailable(),
                hasCaptions = player.captionsAvailable()
            )
        )
    }

    private fun processPlaybackStateChanged(player: Player) {
        val state = when (player.playbackState) {
            Player.STATE_IDLE -> PlayerEvent.Idle
            Player.STATE_BUFFERING -> PlayerEvent.Buffering(
                duration = player.duration,
                position = player.currentPosition,
            )

            Player.STATE_READY -> {
                val playWhenReady = player.playWhenReady
                PlayerEvent.PlayingChanged(
                    playing = playWhenReady,
                    duration = player.duration,
                    position = player.currentPosition,
                )
            }

            Player.STATE_ENDED -> PlayerEvent.Ended
            else -> error("")
        }

        state.let {
            onPlayerEvent(it)
        }
    }

    private fun processPlayWhenReadyChanged(player: Player) {
        processPlaybackStateChanged(player)
    }


    private fun processPlayerError(error: PlaybackException?) {
        onPlayerEvent(
            PlayerEvent.PlayerError(
                error = error
            )
        )
    }

    open fun onPlayerEvent(event: PlayerEvent) {
        if (lastEvent == event) {
            return
        }

        lastEvent = event
        // override here
    }


}


