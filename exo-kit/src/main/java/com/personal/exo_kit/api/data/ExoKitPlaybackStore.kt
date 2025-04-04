package com.personal.exo_kit.api.data

import androidx.media3.common.PlaybackException
import com.personal.exo_kit.internal.data.PlayerName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull

interface ExoKitPlaybackStore {
    val state: StateFlow<GlobalVideoState>
    fun observeEffects(): Flow<PlayerEffect>
}

fun StateFlow<GlobalVideoState>.observe(mediaId: String): Flow<PlaybackState> {
    return mapNotNull { it.playbacks.getOrDefault(mediaId, PlaybackState()) }
        .distinctUntilChanged()
}

fun StateFlow<GlobalVideoState>.latest(mediaId: String): PlaybackState {
    return value.playbacks.getOrDefault(mediaId, PlaybackState())
}

sealed interface PlayerEffect {
    data object VideoRestarted : PlayerEffect
}

sealed interface PlayerState {
    data object Idle : PlayerState
    data object Buffering : PlayerState
    data object Playing : PlayerState
    data object Paused : PlayerState
    data object Ready : PlayerState
    data object Ended : PlayerState
    data class Error(val exception: PlaybackException?) : PlayerState
}

enum class AudioTrackState {
    HAS_SOUND,
    HAS_NO_SOUND,
    UNKNOWN
}

data class GlobalVideoState(
    val playbacks: Map<String, PlaybackState>,
    val exoPlayersCount: Int,
    val lastPlayed: String,
)

data class PlaybackState(
    val playerState: PlayerState = PlayerState.Idle,
    val duration: Long? = null,
    val position: Long? = null,
    val audio: AudioTrackState = AudioTrackState.UNKNOWN,
    val snapshotTimestamp: Long? = null,
    val playerName: PlayerName? = null,
    val playbackSpeed: Float = 1f
)

fun PlaybackState.isPausedOrIdle(): Boolean =
    playerState == PlayerState.Paused || playerState == PlayerState.Idle

fun PlaybackState.isPlaying() = playerState == PlayerState.Playing
fun PlaybackState.isBuffering() = playerState == PlayerState.Buffering
fun PlaybackState.isEnded() = playerState == PlayerState.Ended
