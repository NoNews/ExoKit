package com.personal.consumer_integration.blocks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.personal.consumer_integration.R
import com.personal.consumer_integration.di.rememberMediaSettings
import com.personal.exo_kit.api.data.ActiveVideo
import com.personal.exo_kit.api.data.isBuffering
import com.personal.exo_kit.api.data.isEnded
import com.personal.exo_kit.api.data.isPlaying
import com.personal.exo_kit.api.ui.rememberActiveVideoMediator
import com.personal.exo_kit.api.ui.rememberVideoPlaybackState
import com.personal.exo_kit.api.ui.rememberWishes
import com.personal.exo_kit.internal.data.store.wish.ConsumerWish

@Composable
fun VideoPlayBlock(
    mediaId: String,
    modifier: Modifier = Modifier,
) {
    val settings = rememberMediaSettings()
    val state = rememberVideoPlaybackState(mediaId)
    val wishes = rememberWishes()
    val activeVideoMediator = rememberActiveVideoMediator()


    val autoplay by settings.observeAutoplaySession().collectAsState()
    val activeVideo by activeVideoMediator.observeActiveVideo()
        .collectAsState()

    val activeVideoId = (activeVideo as? ActiveVideo.Known)?.mediaId
    val videoActive = activeVideoId == mediaId

//    if (!videoActive) {
//        return
//    }
//
    if (autoplay) {
        return
    }

    if (state.isBuffering() || state.isPlaying() || state.isEnded()) {
        return
    }

    Box(
        modifier = modifier
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier
                .size(48.dp)
                .clickable {
                    wishes.wish(
                        mediaId = mediaId,
                        wish = ConsumerWish.Play
                    )
                }
                .testTag("post_media_play_icon"),
            painter = painterResource(id = R.drawable.play_button),
            contentDescription = "Replay",
            tint = Color.White
        )
    }


}