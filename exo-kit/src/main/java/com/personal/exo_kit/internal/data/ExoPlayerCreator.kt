package com.personal.exo_kit.internal.data

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer

class ExoPlayerCreator {
    fun createWithRecommendedParams(context: Context): ExoPlayer.Builder {
        return ExoPlayer.Builder(context)
    }
}