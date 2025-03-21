package com.personal.exo_kit.internal.data.store.mutator

import com.personal.exo_kit.api.data.AudioTrackState
import com.personal.exo_kit.api.data.PlayerState
import com.personal.exo_kit.internal.data.ExoKitPlayer

interface PlaybackMutator {
    fun mutate(mutation: PlaybackMutation)
}

sealed class PlaybackMutation {
    abstract val mediaId: String

    data class SetState(
        override val mediaId: String,
        val state: PlayerState,
        val duration: Long,
        val position: Long,
    ) : PlaybackMutation()

    data class UpdateTracks(override val mediaId: String, val state: AudioTrackState) :
        PlaybackMutation()

    data class AssociationChanged(override val mediaId: String, val player: ExoKitPlayer?) :
        PlaybackMutation()

    sealed class EffectMutation : PlaybackMutation() {
        data class VideoRestarted(override val mediaId: String) : EffectMutation()
    }
}
