package com.personal.exo_kit.api.extensions

import android.annotation.SuppressLint
import androidx.media3.common.MimeTypes
import androidx.media3.common.PlaybackException
import androidx.media3.exoplayer.ExoPlaybackException
import androidx.media3.exoplayer.mediacodec.MediaCodecDecoderException
import androidx.media3.exoplayer.mediacodec.MediaCodecInfo
import androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
import androidx.media3.exoplayer.mediacodec.MediaCodecUtil

@SuppressLint("UnsafeOptInUsageError")
fun getDecodersInfo(mimeType: String): Map<Boolean, List<MediaCodecInfo>> {
    val result = runCatching {
        val info = MediaCodecUtil.getDecoderInfos(mimeType, false, false)
        val result = info.toList()
            .groupBy { it.hardwareAccelerated }
        result
    }
    return result.getOrNull() ?: emptyMap()
}

@SuppressLint("UnsafeOptInUsageError")
data class DecodersInfo(
    val playbackDecoder: MediaCodecInfo?,
    val allDecoders: Map<Boolean, List<MediaCodecInfo>>,
    val isRecoverable: Boolean,
)

@SuppressLint("UnsafeOptInUsageError")
fun PlaybackException.extractDecodersInfo(): DecodersInfo {
    var playbackDecoder: MediaCodecInfo? = null
    var isRecoverable: Boolean = false
    runCatching {
        if (this is ExoPlaybackException) {
            val rendererException = this.rendererException
            if (rendererException is MediaCodecDecoderException) {
                val decoderInfo = rendererException.codecInfo
                playbackDecoder = decoderInfo
                isRecoverable = false
            } else if (rendererException is MediaCodecRenderer.DecoderInitializationException) {
                val decoderInfo = rendererException.codecInfo
                playbackDecoder = decoderInfo
                isRecoverable = true
            }
        }
    }

    val allDecoders = getDecodersInfo(MimeTypes.VIDEO_H264)
    return DecodersInfo(
        playbackDecoder = playbackDecoder,
        allDecoders = allDecoders,
        isRecoverable = isRecoverable,
    )
}


@SuppressLint("UnsafeOptInUsageError")
fun DecodersInfo.toErrorReportString(): String {
    val playbackDecoderName = playbackDecoder?.name ?: "Unknown"
    val playbackDecoderType = playbackDecoder?.isSoftware() ?: "Unknown"

    // Filter out the playback decoder from the list
    val otherDecoders = allDecoders.values.flatten()
        .filter { it.name != playbackDecoderName }
        .map { "${it.name}, software=${it.isSoftware()}" }

    return buildString {
        append("Used decoder: $playbackDecoderName, software=$playbackDecoderType. ")
        if (otherDecoders.isNotEmpty()) {
            append("Other available decoders: ${otherDecoders.joinToString("; ")}")
        } else {
            append("No other available decoders.")
        }
    }
}

// Helper function to determine if a decoder is software-based
@SuppressLint("UnsafeOptInUsageError")
fun MediaCodecInfo.isSoftware(): Boolean {
    return !hardwareAccelerated
}