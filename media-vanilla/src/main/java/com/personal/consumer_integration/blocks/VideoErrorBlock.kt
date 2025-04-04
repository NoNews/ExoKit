package com.personal.consumer_integration.blocks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.personal.exo_kit.api.data.PlayerState
import com.personal.exo_kit.api.data.isError
import com.personal.exo_kit.api.ui.rememberVideoPlaybackState


@Composable
fun VideoErrorBlock(mediaId: String, modifier: Modifier) {
    val playbackState = rememberVideoPlaybackState(mediaId)
    if (playbackState.isError()) {
        Box(
            modifier = modifier
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            val errorText = (playbackState.playerState as? PlayerState.Error)?.exception?.errorCode
            Text("Playback error: error code: $errorText")
        }
    }
}