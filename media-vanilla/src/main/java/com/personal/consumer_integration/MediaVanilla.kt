@file:OptIn(ExperimentalGlideComposeApi::class)

package com.personal.consumer_integration

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.personal.consumer_integration.blocks.DebugBlock
import com.personal.consumer_integration.blocks.MuteButtonBlock
import com.personal.consumer_integration.blocks.VideoEndedBlock
import com.personal.consumer_integration.blocks.VideoLoadingBlock
import com.personal.consumer_integration.di.MediaDI
import com.personal.consumer_integration.extensions.onVisibilityChanged
import com.personal.consumer_integration.internal.MediaVanillaViewModel
import com.personal.exo_kit.api.data.PlayerState
import com.personal.exo_kit.api.ui.ExoKitVideo
import com.personal.exo_kit.api.ui.params.ExoKitVideoProps
import com.personal.exo_kit.api.ui.params.VideoSurfaceLifecycle
import com.personal.exo_kit.api.ui.rememberActiveVideoMediator
import com.personal.exo_kit.api.ui.rememberVideoPlaybackState
import com.personal.exo_kit.api.ui.rememberVideoPlaybackStore
import com.personal.exo_kit.api.ui.rememberWishes
import com.personal.exo_kit.api.ui.resizeWithContentScale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


@Composable
fun MediaVanilla(props: MediaVanillaProps, modifier: Modifier) {
    val store = rememberVideoPlaybackStore()
    val wish = rememberWishes()
    val activeVideoMediator = rememberActiveVideoMediator()
    val viewModel = remember {
        MediaVanillaViewModel(
            playbackStore = store,
            scope = CoroutineScope(Dispatchers.Main.immediate),
            mediaSettingsStore = MediaDI.mediaSettingsStore,
            activeVideoMediator = activeVideoMediator,
            wishes = wish,
            props = props,
        )
    }
    val mediaId = props.mediaId
    val state = rememberVideoPlaybackState(mediaId)


    var videoVisibilityFraction by remember { mutableFloatStateOf(0f) }


    DisposableEffect(Unit) {
        onDispose {
            viewModel.onCleared()
        }
    }

    val lifecycle = VideoSurfaceLifecycle.current
    LaunchedEffect(key1 = videoVisibilityFraction, key2 = lifecycle) {
        viewModel.onLifecycleChanged(videoVisibilityFraction, lifecycle)
    }


    Box(
        modifier.resizeWithContentScale(
            contentScale = props.scale,
            sourceSizeDp = Size(props.width.toFloat(), props.height.toFloat())
        )
    ) {
        GlideImage(
            model = props.thumbnailUrl,
            contentDescription = "Video Thumbnail",
            contentScale = props.scale,
            modifier = Modifier.fillMaxSize()
        )

        ExoKitVideo(
            modifier = Modifier
                .onVisibilityChanged { visibilityPercentage ->
                    videoVisibilityFraction = visibilityPercentage
                },
            props = ExoKitVideoProps(
                mediaId = props.mediaId,
                url = props.videoUrl,
                sessionId = "sessionId",
                surfaceId = props.surfaceName,
                loop = props.loop,
                position = props.position,
            )
        )


        DebugBlock(
            mediaId = props.mediaId,
            videoVisibilityFraction = videoVisibilityFraction,
            modifier = Modifier.align(Alignment.TopStart)
        )


        VideoEndedBlock(
            mediaId = props.mediaId,
        )

        MuteButtonBlock(
            mediaId = props.mediaId,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(4.dp)
        )

        VideoLoadingBlock(
            mediaId = props.mediaId,
            modifier = Modifier.align(Alignment.Center)
        )


        when (val state = state.playerState) {
            is PlayerState.Buffering -> {
            }

            is PlayerState.Error -> {
                Text(
                    text = "Playback error! ${state.exception?.errorCode}, ${state.exception?.errorCodeName}",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            PlayerState.Idle -> {

            }

            PlayerState.Paused -> {

            }

            PlayerState.Playing -> {

            }

            PlayerState.Ready -> {

            }

            PlayerState.Ended -> {

            }
        }
    }
}