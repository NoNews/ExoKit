package com.personal.consumer_integration

import androidx.compose.runtime.Immutable
import androidx.compose.ui.layout.ContentScale


@Immutable
data class MediaVanillaProps(
    val mediaId: String,
    val videoUrl: String,
    val thumbnailUrl: String,
    val width: Int,
    val height: Int,
    val controls: Controls = Controls.None,
    val clickAction: ClickAction = ClickAction.DoNothing,
    val loop: Boolean,
    val surfaceName: String,
    val scale: ContentScale,
    val autoplayMode: AutoplayMode,
    val position: Int,
)

enum class AutoplayMode {
    APP_SETTINGS,
    ENABLED,
    DISABLED,
    BLURRED,
}


sealed class ClickAction {
    data object DoNothing : ClickAction()
    data object PostDetail : ClickAction()
    data object FullBleedPlayer : ClickAction()
    // other actions
}

sealed class Controls {
    data object None : Controls()
    data object Default : Controls()
}