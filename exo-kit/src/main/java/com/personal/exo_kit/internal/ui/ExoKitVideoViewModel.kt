package com.personal.exo_kit.internal.ui

import com.personal.exo_kit.api.data.ActiveVideoMediator
import com.personal.exo_kit.api.data.AssociationListener
import com.personal.exo_kit.api.data.AudioTrackState
import com.personal.exo_kit.api.data.PlaybackState
import com.personal.exo_kit.api.data.PlayerState
import com.personal.exo_kit.api.ui.params.ExoKitVideoProps
import com.personal.exo_kit.api.ui.params.SurfaceLifecycle
import com.personal.exo_kit.internal.data.ExoKitPlayer
import com.personal.exo_kit.internal.data.coordinator.ExoKitPlaybackCoordinator
import com.personal.exo_kit.internal.data.coordinator.ExoKitPriorityCoordinator
import com.personal.exo_kit.internal.data.coordinator.PlaybackKey
import com.personal.exo_kit.internal.data.coordinator.listener.PlayerEvent
import com.personal.exo_kit.internal.data.coordinator.listener.PlayerListener
import com.personal.exo_kit.internal.data.store.ExoKitPlaybackStoreImpl
import com.personal.exo_kit.internal.data.store.mutator.PlaybackMutation
import com.personal.exo_kit.internal.data.store.wish.ConsumerWish
import com.personal.exo_kit.internal.data.store.wish.ConsumerWishesImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal class ExoKitVideoViewModel(
    private val playbackCoordinator: ExoKitPlaybackCoordinator,
    private val priorityCoordinator: ExoKitPriorityCoordinator,
    private val props: ExoKitVideoProps,
    private val playbackStore: ExoKitPlaybackStoreImpl,
    private val wishesImpl: ConsumerWishesImpl,
    private val activeVideoMediator: ActiveVideoMediator,
) : PlayerListener(), AssociationListener {

    private val scope = CoroutineScope(Dispatchers.Main)
    private var surface: ExoKitVideoSurface? = null
    private var wasEverActive: Boolean = false

    private val playbackKey = PlaybackKey(
        mediaId = props.mediaId,
        surfaceId = props.surfaceId,
    )

    fun onLifecycle(lifecycle: SurfaceLifecycle) {
        when (lifecycle) {
            SurfaceLifecycle.SURFACE_ACTIVE -> {
                activate()
            }

            SurfaceLifecycle.SURFACE_INACTIVE -> {
                deactivate()
            }
        }
    }


    private fun activate() {
        println("ExoKit:VideoViewModel:act:activate, ${props.surfaceId}#${props.mediaId}")

        playbackCoordinator.registerAssociationListener(this)

        val prewarmed = playbackCoordinator.prewarm(
            loop = props.loop,
            url = props.url,
            mediaId = props.mediaId,
            sessionId = props.sessionId,
            surfaceId = props.surfaceId,
            position = props.position,
        )

        if (prewarmed) {
            println("ExoKit:VideoViewModel:act:activate, ${props.surfaceId}#${props.mediaId}, prewarm scheduled successfully")
            playbackCoordinator.loop(
                mediaId = props.mediaId,
                loop = props.loop,
                surfaceId = props.surfaceId,
            )
            playbackCoordinator.setSurface(
                mediaId = props.mediaId,
                surface = surface!!,
                surfaceId = props.surfaceId
            )
        }

        scope.launch {
            priorityCoordinator.currentActive.collectLatest { activePlayback ->
                onEligiblePlaybackChanged(activePlayback)
            }
        }
        scope.launch {
            wishesImpl.consumerWishes.observe(props.mediaId)
                .collect { wish ->
                    when (wish) {
                        ConsumerWish.Play -> playbackCoordinator.play(
                            mediaId = props.mediaId,
                            url = props.url,
                            sessionId = "",
                            position = props.position,
                            loop = props.loop,
                        )

                        ConsumerWish.Pause -> playbackCoordinator.pause(
                            mediaId = props.mediaId,
                            surfaceId = props.surfaceId,
                        )

                        is ConsumerWish.SeekPosition -> playbackCoordinator.seekTo(
                            mediaId = props.mediaId,
                            position = wish.position
                        )

                        is ConsumerWish.SeekFraction -> playbackCoordinator.seekToFraction(
                            mediaId = props.mediaId,
                            fraction = wish.fraction.toLong()
                        )

                        is ConsumerWish.PriorityChanged -> priorityCoordinator.onPriorityChanged(
                            playbackKey = playbackKey,
                            visibility = wish.fraction,
                            position = props.position
                        )

                        is ConsumerWish.ToggleMute -> {
                            playbackCoordinator.mute(props.mediaId, wish.mute)
                        }

                        is ConsumerWish.Retry -> {
                            playbackCoordinator.retry(
                                mediaId = props.mediaId,
                                url = props.url,
                                surfaceId = props.surfaceId
                            )
                        }
                    }
                }
        }
    }


    fun setSurface(surface: ExoKitVideoSurface) {
        this.surface = surface
        println("ExoKit:VideoViewModel:act:setSurface, ${props.surfaceId}#${props.mediaId} successfully ")
    }

    override fun onAssociationChanged(association: LinkedHashMap<String, ExoKitPlayer>) {
        val player = association[props.mediaId]
        onPlayerEvent(event = PlayerEvent.AssociationChanged(player))
    }

    private fun onEligiblePlaybackChanged(activePlayback: PlaybackKey?) {
        if (playbackKey == activePlayback) {
            playbackCoordinator.registerPlaybackListener(props.mediaId, this)
            activeVideoMediator.setActive(props.mediaId)
            playbackCoordinator.setSurface(
                mediaId = props.mediaId,
                surface = surface!!,
                surfaceId = props.surfaceId
            )
            playbackCoordinator.play(
                mediaId = props.mediaId,
                url = props.url,
                sessionId = "",
                position = props.position,
                loop = props.loop,
            )
            wasEverActive = true
            return
        } else if (wasEverActive) {
            playbackCoordinator.removeSurface(mediaId = props.mediaId, surfaceId = props.surfaceId)
            playbackCoordinator.unregisterPlaybackListener(props.mediaId, this)
            wasEverActive = false
        }
    }

    override fun onPlayerEvent(event: PlayerEvent) {
        super.onPlayerEvent(event)

        val lastPlaybackState =
            playbackStore.state.value.playbacks.getOrDefault(
                props.mediaId, PlaybackState()
            )

        val mediaId = props.mediaId
        val mutation = when (event) {
            is PlayerEvent.PlayingChanged -> {
                val playing = event.playing
                PlaybackMutation.SetState(
                    mediaId = mediaId,
                    state = if (playing) PlayerState.Playing else PlayerState.Paused,
                    duration = event.duration,
                    position = event.position
                )
            }

            is PlayerEvent.Buffering -> {
                PlaybackMutation.SetState(
                    mediaId = mediaId,
                    state = PlayerState.Buffering,
                    duration = event.duration,
                    position = event.position,
                )
            }

            is PlayerEvent.TracksChanged -> {
                val audioTrackState = if (event.hasSound) {
                    AudioTrackState.HAS_SOUND
                } else {
                    AudioTrackState.HAS_NO_SOUND
                }
                PlaybackMutation.UpdateTracks(
                    mediaId = mediaId,
                    state = audioTrackState,
                )
            }

            is PlayerEvent.Idle -> {
                PlaybackMutation.SetState(
                    mediaId = mediaId,
                    state = PlayerState.Idle,
                    duration = lastPlaybackState.duration ?: 0,
                    position = lastPlaybackState.position ?: 0,
                )
            }

            is PlayerEvent.Ready -> {
                PlaybackMutation.SetState(
                    mediaId = mediaId,
                    state = PlayerState.Ready,
                    duration = event.duration,
                    position = lastPlaybackState.position ?: 0,
                )
            }

            is PlayerEvent.PlayerError -> {
                PlaybackMutation.SetState(
                    mediaId = mediaId,
                    state = PlayerState.Error(event.error),
                    duration = lastPlaybackState.duration ?: 0,
                    position = lastPlaybackState.position ?: 0,
                )
            }

            is PlayerEvent.AssociationChanged -> {
                PlaybackMutation.AssociationChanged(
                    mediaId = mediaId,
                    player = event.player
                )
            }

            is PlayerEvent.Ended -> {
                PlaybackMutation.SetState(
                    mediaId = mediaId,
                    state = PlayerState.Ended,
                    duration = lastPlaybackState.duration ?: 0,
                    position = lastPlaybackState.duration ?: 0,
                )
            }

            is PlayerEvent.VideoLooped -> {
                PlaybackMutation.EffectMutation.VideoRestarted(
                    mediaId = mediaId,
                )
            }

            else -> return
        }
        playbackStore.mutate(mutation)
    }

    private fun deactivate() {
        println("ExoKit:ViewModel:act:deactivate, ${props.mediaId} -> ${props.surfaceId}, ${hashCode()}")
        playbackCoordinator.removeSurface(mediaId = props.mediaId, surfaceId = props.surfaceId)
        priorityCoordinator.remove(playbackKey)
        playbackCoordinator.unregisterAssociationListener(this)
        playbackCoordinator.unregisterPlaybackListener(props.mediaId, this)
        scope.coroutineContext.cancelChildren()
        wasEverActive = false
    }

    fun destroy() {
        println("ExoKit:ViewModel:act:destroy, ${props.surfaceId}, mediaId: ${props.mediaId}")
        deactivate()
    }

    fun releaseSurface() {
        println("ExoKit:ViewModel:act:releaseSurface, ${props.surfaceId}, mediaId: ${props.mediaId}")
        surface = null
    }
}