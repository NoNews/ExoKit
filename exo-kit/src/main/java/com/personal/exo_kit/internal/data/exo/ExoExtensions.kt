package com.personal.exo_kit.internal.data.exo

import android.annotation.SuppressLint
import androidx.media3.common.C
import androidx.media3.common.C.TRACK_TYPE_AUDIO
import androidx.media3.common.C.TRACK_TYPE_TEXT
import androidx.media3.common.C.TRACK_TYPE_VIDEO
import androidx.media3.common.Format
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import com.personal.exo_kit.internal.data.coordinator.listener.TrackInfo


@SuppressLint("UnsafeOptInUsageError")
internal fun Player.getTracksInfo(): List<TrackInfo> {
    if (this !is ExoPlayer) {
        return emptyList()
    }

    val tracks = mutableListOf<TrackInfo>()
    this.currentTracks.groups.forEach { group ->
        for (i in 0 until group.length) {
            val trackFormat: Format = group.getTrackFormat(i)
            when (group.type) {
                TRACK_TYPE_VIDEO -> {
                    val video = TrackInfo.Video(
                        width = trackFormat.width,
                        height = trackFormat.height,
                        bitrate = trackFormat.bitrate,
                        codecs = trackFormat.codecs ?: UNKNOWN,
                        frameRate = trackFormat.frameRate,
                    )
                    tracks += video
                }

                TRACK_TYPE_AUDIO -> {
                    val audio = TrackInfo.Audio(
                        codecs = trackFormat.codecs ?: UNKNOWN,
                        containerMimeType = trackFormat.containerMimeType ?: UNKNOWN,
                        bitrate = trackFormat.bitrate,
                        peekBitrate = trackFormat.peakBitrate,
                        language = trackFormat.language ?: UNKNOWN,
                        channelCount = trackFormat.channelCount,
                        sampleRate = trackFormat.sampleRate,
                    )
                    tracks += audio
                }

                TRACK_TYPE_TEXT -> {
                    val text = TrackInfo.Text(
                        codecs = trackFormat.codecs ?: UNKNOWN,
                        language = trackFormat.language ?: UNKNOWN,
                        sampleMimeType = trackFormat.sampleMimeType ?: UNKNOWN,
                    )
                    tracks += text
                }
            }
        }
    }
    return tracks
}

const val UNKNOWN = "unknown"

internal fun Player.soundAvailable(): Boolean {
    if (this !is ExoPlayer) {
        return false
    }
    val trackGroups = this.currentTracks.groups
    if (trackGroups.isEmpty()) {
        return false
    }
    for (group in 0 until trackGroups.size) {
        for (format in 0 until trackGroups[group].length) {
            if (trackGroups[group].getTrackFormat(format).sampleMimeType?.contains("audio") == true) {
                return true
            }
        }
    }
    return false
}

@SuppressLint("UnsafeOptInUsageError")
internal fun Player.captionsAvailable(): Boolean {
    if (this is ExoPlayer) {
        val trackSelector = trackSelector as? DefaultTrackSelector ?: return false
        val trackInfo = trackSelector.currentMappedTrackInfo ?: return false
        val textRendererIndex = (0 until trackInfo.rendererCount)
            .firstOrNull { trackInfo.getRendererType(it) == C.TRACK_TYPE_TEXT } ?: return false
        if (trackInfo.getTrackGroups(textRendererIndex).isEmpty) {
            return false
        }
        val trackGroups = trackInfo.getTrackGroups(textRendererIndex)

        return !trackGroups.isEmpty
    }
    return false
}