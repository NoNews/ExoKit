package com.personal.exo_kit.api.ui


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.personal.exo_kit.api.ui.params.ExoKitVideoProps
import com.personal.exo_kit.api.ui.params.VideoSurfaceLifecycle
import com.personal.exo_kit.internal.data.DI
import com.personal.exo_kit.internal.data.store.wish.ConsumerWishesImpl
import com.personal.exo_kit.internal.ui.ExoKitVideoViewModel
import com.personal.exo_kit.internal.ui.ExoKitVideoSurface

@Composable
fun ExoKitVideo(
    modifier: Modifier = Modifier,
    props: ExoKitVideoProps,
) {
    val viewModel = remember {
        ExoKitVideoViewModel(
            playbackCoordinator = DI.playbackCoordinator,
            priorityCoordinator = DI.visibilityCoordinator,
            props = props,
            playbackStore = DI.playbackStore,
            activeVideoMediator = DI.activeVideoMediator,
            wishesImpl = DI.consumerWishes as ConsumerWishesImpl,
        )
    }
    Box(modifier) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = ::ExoKitVideoSurface,
            update = viewModel::setSurface,
            onRelease = {
                viewModel.releaseSurface()
            }
        )

        val lifecycle = VideoSurfaceLifecycle.current
        LaunchedEffect(lifecycle) {
            viewModel.onLifecycle(lifecycle)
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            viewModel.destroy()
        }
    }
}




