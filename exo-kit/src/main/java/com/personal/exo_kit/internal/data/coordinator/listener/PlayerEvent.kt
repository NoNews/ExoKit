package com.personal.exo_kit.internal.data.coordinator.listener

import androidx.media3.common.PlaybackException
import com.personal.exo_kit.internal.data.ExoKitPlayer

sealed class PlayerEvent {

    class AssociationChanged(val player: ExoKitPlayer?) : PlayerEvent()
    data class Buffering(val duration: Long, val position: Long) : PlayerEvent()
    data object Idle : PlayerEvent()
    data class Ready(val playWhenReady: Boolean, val duration: Long) : PlayerEvent()
    data object Ended : PlayerEvent()
    data class PlayingChanged(val playing: Boolean, val duration: Long, val position: Long) :
        PlayerEvent()

    data class PlayerError(val error: PlaybackException?) : PlayerEvent()
    data class TracksChanged(
        val tracks: List<TrackInfo>,
        val hasSound: Boolean,
        val hasCaptions: Boolean,
    ) : PlayerEvent()


    data class SurfaceSizeChanged(val width: Int, val height: Int) : PlayerEvent()
    data class VideoSizeChanged(val width: Int, val height: Int) : PlayerEvent()
    data object VideoLooped : PlayerEvent()
    data object RenderedFirstFrame : PlayerEvent()
    data class Cues(val cues: List<String>) : PlayerEvent()
}

sealed interface TrackInfo {
    data class Video(
        val width: Int,
        val height: Int,
        val bitrate: Int,
        val codecs: String,
        val frameRate: Float,
    ) : TrackInfo {
        override fun toString(): String {
            return """
                Video Track:
                Width: $width
                Height: $height
                Bitrate: $bitrate
                Codecs: $codecs
                Frame Rate: $frameRate
      """.trimIndent()
        }
    }

    data class Text(
        val codecs: String,
        val language: String,
        val sampleMimeType: String,
    ) : TrackInfo {
        override fun toString(): String {
            return """
                Text Track:
                Codecs: $codecs
                Language: $language
                MIME Type: $sampleMimeType
      """.trimIndent()
        }
    }

    data class Audio(
        val codecs: String,
        val containerMimeType: String,
        val bitrate: Int,
        val peekBitrate: Int,
        val language: String,
        val channelCount: Int,
        val sampleRate: Int,
    ) : TrackInfo {
        override fun toString(): String {
            return """
                Audio Track:
                Codecs: $codecs
                Container MIME Type: $containerMimeType
                Bitrate: $bitrate
                Peak Bitrate: $peekBitrate
                Language: $language
                Channels: $channelCount
                Sample Rate: $sampleRate
      """.trimIndent()
        }
    }
}

fun List<TrackInfo>.hasSound() = any { it is TrackInfo.Audio }
fun List<TrackInfo>.hasCaptions() = any { it is TrackInfo.Text }
