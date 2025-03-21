package com.personal.consumer_integration.blocks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.personal.exo_kit.api.data.ActiveVideo
import com.personal.exo_kit.api.data.isBuffering
import com.personal.exo_kit.api.ui.rememberActiveVideoMediator
import com.personal.exo_kit.api.ui.rememberVideoPlaybackState
import kotlinx.coroutines.delay

@Composable
fun VideoLoadingBlock(
    mediaId: String,
    loadingDelay: Long = 1000,
    modifier: Modifier = Modifier,
) {

    val activeVideoMediator = rememberActiveVideoMediator()
    val activeVideo by activeVideoMediator.observeActiveVideo()
        .collectAsState()

    val state = rememberVideoPlaybackState(mediaId)
    val activeVideoId = (activeVideo as? ActiveVideo.Known)?.mediaId
    val videoActive = activeVideoId == mediaId

    if (!videoActive) {
        return
    }

    val loading = state.isBuffering()

    Box(modifier = modifier.fillMaxSize()) {
        val showLoadingIndicator by produceState(initialValue = false, loading) {
            value = if (loading) {
                delay(loadingDelay)
                true
            } else {
                false
            }
        }

        if (showLoadingIndicator) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .testTag("progress_indicator"),
                color = Color.Cyan,
            )
        }
    }
}