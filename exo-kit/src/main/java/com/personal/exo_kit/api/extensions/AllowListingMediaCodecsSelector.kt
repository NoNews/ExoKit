package com.personal.exo_kit.api.extensions

import android.annotation.SuppressLint
import androidx.media3.exoplayer.mediacodec.MediaCodecInfo
import androidx.media3.exoplayer.mediacodec.MediaCodecSelector

@SuppressLint("UnsafeOptInUsageError")
class AllowListingMediaCodecsSelector : MediaCodecSelector {

    private val dirtyCodecs = mutableSetOf<String>()
    override fun getDecoderInfos(
        mimeType: String,
        requiresSecureDecoder: Boolean,
        requiresTunnelingDecoder: Boolean
    ): MutableList<MediaCodecInfo> {
        val infos = MediaCodecSelector.DEFAULT.getDecoderInfos(
            /* mimeType = */ mimeType,
            /* requiresSecureDecoder = */ requiresSecureDecoder,
            /* requiresTunnelingDecoder = */ requiresTunnelingDecoder
        ).let { it ->
            it.filter { !dirtyCodecs.contains(it.name) }.toMutableList()
        }

        return infos
    }

    fun markAsDirty(codec: String) {
        synchronized(this) {
            dirtyCodecs += codec
        }
    }
}