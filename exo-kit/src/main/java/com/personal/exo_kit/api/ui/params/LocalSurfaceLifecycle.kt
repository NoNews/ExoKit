package com.personal.exo_kit.api.ui.params

import androidx.compose.runtime.staticCompositionLocalOf

val VideoSurfaceLifecycle = staticCompositionLocalOf { SurfaceLifecycle.SURFACE_ACTIVE }

enum class SurfaceLifecycle {
    SURFACE_ACTIVE,
    SURFACE_INACTIVE,
}