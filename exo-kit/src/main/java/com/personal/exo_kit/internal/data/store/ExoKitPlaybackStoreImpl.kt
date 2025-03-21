package com.personal.exo_kit.internal.data.store

import com.personal.exo_kit.api.data.ExoKitPlaybackStore
import com.personal.exo_kit.api.data.GlobalVideoState
import com.personal.exo_kit.api.data.PlaybackState
import com.personal.exo_kit.api.data.PlayerEffect
import com.personal.exo_kit.internal.data.store.mutator.PlaybackMutation
import com.personal.exo_kit.internal.data.store.mutator.PlaybackMutator
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

internal class ExoKitPlaybackStoreImpl(
) : ExoKitPlaybackStore, PlaybackMutator {
    override val state: StateFlow<GlobalVideoState>
        get() = _state

    private val _state = MutableStateFlow(
        GlobalVideoState(
            playbacks = emptyMap(),
            exoPlayersCount = 0,
            lastPlayed = ""
        )
    )
    private val effects = MutableSharedFlow<PlayerEffect>(
        replay = 2,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    override fun observeEffects(): Flow<PlayerEffect> = effects


    override fun mutate(mutation: PlaybackMutation) {
        val mediaId = mutation.mediaId
        when (mutation) {
            is PlaybackMutation.EffectMutation.VideoRestarted -> {
                effects.tryEmit(PlayerEffect.VideoRestarted)
            }

            is PlaybackMutation.UpdateTracks -> _state.update { globalState ->
                val playbacksToUpdate = globalState.playbacks.toMutableMap()
                val currentPlaybackState = playbacksToUpdate.getOrDefault(mediaId, PlaybackState())
                val newPlaybackState = currentPlaybackState.copy(
                    audio = mutation.state,
                    snapshotTimestamp = System.currentTimeMillis()
                )
                playbacksToUpdate[mediaId] = newPlaybackState
                globalState.copy(playbacks = playbacksToUpdate)
            }

            is PlaybackMutation.SetState -> _state.update { globalState ->
                val playbacksToUpdate = globalState.playbacks.toMutableMap()
                val currentPlaybackState = playbacksToUpdate.getOrDefault(mediaId, PlaybackState())
                val newPlaybackState = currentPlaybackState.copy(
                    playerState = mutation.state,
                    duration = mutation.duration,
                    position = mutation.position,
                    snapshotTimestamp = System.currentTimeMillis()
                )
                playbacksToUpdate[mediaId] = newPlaybackState
                globalState.copy(playbacks = playbacksToUpdate)
            }

            is PlaybackMutation.AssociationChanged -> {
                _state.update { globalState ->
                    val playbacksToUpdate = globalState.playbacks.toMutableMap()
                    val currentPlaybackState =
                        playbacksToUpdate.getOrDefault(mediaId, PlaybackState())
                    val newPlaybackState = currentPlaybackState.copy(
                        playerName = mutation.player?.name,
                    )
                    playbacksToUpdate[mediaId] = newPlaybackState
                    globalState.copy(playbacks = playbacksToUpdate)
                }
            }
        }
    }
}