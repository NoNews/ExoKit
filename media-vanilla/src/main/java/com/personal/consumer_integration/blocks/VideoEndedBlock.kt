package com.personal.consumer_integration.blocks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.personal.exo_kit.api.data.isEnded
import com.personal.exo_kit.api.ui.rememberVideoPlaybackState
import com.personal.consumer_integration.R

@Composable
fun VideoEndedBlock(
    mediaId: String,
    showShadow: Boolean = true,
    modifier: Modifier = Modifier
) {
    val state = rememberVideoPlaybackState(mediaId)

    if (!state.isEnded()) {
        return
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .ifThen(showShadow) {
                background(Color.Black.copy(alpha = 0.5f))
            },
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier.clickable {
            }
        ) {
            IconButton(
                onClick = { },
                modifier = Modifier
                    .size(48.dp)
                    .testTag("post_media_play_icon")
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.replay_button),
                    contentDescription = "Replay",
                    tint = Color.White
                )
            }
        }

    }
}

inline fun Modifier.ifThen(
    flag: Boolean,
    action: Modifier.() -> Modifier,
) = ifThen(
    predicate = { flag },
    action = action,
)

inline fun Modifier.ifThen(
    predicate: () -> Boolean,
    action: Modifier.() -> Modifier,
): Modifier = run {
    if (predicate()) this.action() else this
}
