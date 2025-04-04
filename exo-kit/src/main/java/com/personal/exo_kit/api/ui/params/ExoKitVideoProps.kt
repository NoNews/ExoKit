package com.personal.exo_kit.api.ui.params

import androidx.compose.runtime.Immutable

@Immutable
data class ExoKitVideoProps(
    val url: String,
    val mediaId: String,
    val sessionId: String,
    val loop: Boolean = true,
    val surfaceId: String,
    val position: Int,
)