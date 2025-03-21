package com.personal.consumer_integration

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.personal.exo_kit.api.ui.params.VideoSurfaceLifecycle
import com.personal.exo_kit.api.ui.params.SurfaceLifecycle

@Composable
fun SurfaceLifecycleProvider(content: @Composable () -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val surfaceState = remember { mutableStateOf(SurfaceLifecycle.SURFACE_ACTIVE) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    surfaceState.value = SurfaceLifecycle.SURFACE_ACTIVE
                }

                Lifecycle.Event.ON_PAUSE -> {
                    surfaceState.value = SurfaceLifecycle.SURFACE_INACTIVE
                }

                else -> {}
            }
        }

        // Add the observer to the lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)

        // Remove the observer when the effect is disposed
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Provide the surface state to the composition
    CompositionLocalProvider(VideoSurfaceLifecycle provides surfaceState.value) {
        content()
    }
}