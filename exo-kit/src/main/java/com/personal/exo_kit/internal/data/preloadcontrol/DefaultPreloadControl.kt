package com.personal.exo_kit.internal.data.preloadcontrol

import android.annotation.SuppressLint
import androidx.media3.exoplayer.source.preload.DefaultPreloadManager
import androidx.media3.exoplayer.source.preload.TargetPreloadStatusControl

@SuppressLint("UnsafeOptInUsageError")
class DefaultPreloadControl : TargetPreloadStatusControl<Int> {

    @SuppressLint("WrongConstant")
    override fun getTargetPreloadStatus(rankingData: Int): DefaultPreloadManager.Status {
        return DefaultPreloadManager.Status(
            DefaultPreloadManager.Status.STAGE_LOADED_FOR_DURATION_MS,
            1_000
        )
    }
}