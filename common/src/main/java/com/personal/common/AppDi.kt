package com.personal.common

import android.annotation.SuppressLint
import android.content.Context
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.source.preload.DefaultPreloadManager
import com.personal.common.settings.AppSettingsRepository
import com.personal.exo_kit.api.data.ExoKitPlayerPool
import com.personal.exo_kit.api.extensions.AllowListingMediaCodecsSelector
import com.personal.exo_kit.internal.data.preloadcontrol.DefaultPreloadControl

@SuppressLint("StaticFieldLeak", "UnsafeOptInUsageError")
object AppDi {
    private lateinit var context: Context

    val loadControl by lazy {
        DefaultLoadControl.Builder()
            .setBufferDurationsMs(
                20_000,
                20_000,
                1_000,
                1_000,
            )
            .setPrioritizeTimeOverSizeThresholds(true)
            .build()
    }

    val preloadManagerBuilder by lazy {
        DefaultPreloadManager.Builder(
            /* context = */ context,
            /* targetPreloadStatusControl = */ DefaultPreloadControl(),
        ).setLoadControl(loadControl)
            .setRenderersFactory(
                DefaultRenderersFactory(context)
                    .setMediaCodecSelector(codecsSelector)
                    .setEnableDecoderFallback(true)
            )
    }

    val codecsSelector by lazy {
        AllowListingMediaCodecsSelector()
    }


    val appSettingsRepository by lazy {
        AppSettingsRepository()
    }

    @SuppressLint("StaticFieldLeak")
    fun initialise(context: Context) {
        AppDi.context = context
    }

    val playerPool by lazy {
        ExoKitPlayerPool(
            context = context,
            poolSize = { 3 },
            playerCreator = {
                preloadManagerBuilder.buildExoPlayer()
            }
        )
    }
}