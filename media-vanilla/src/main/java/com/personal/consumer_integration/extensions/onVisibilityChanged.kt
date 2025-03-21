package com.personal.consumer_integration.extensions


import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned

@Composable
fun Modifier.onVisibilityChanged(
    onVisibilityChanged: (Float) -> Unit
): Modifier {
    var lastVisibility by remember { mutableFloatStateOf(0f) }
    var lastUpdateTime by remember { mutableLongStateOf(System.currentTimeMillis()) }

    return this.then(
        Modifier.onGloballyPositioned { coordinates ->
            val visibilityPercentage = coordinates.getVisibilityPercentage()
            val currentTime = System.currentTimeMillis()
            // Only trigger if 100ms has passed since the last update
            if (visibilityPercentage == 0f || visibilityPercentage == 1f) {
                onVisibilityChanged(visibilityPercentage)
                return@onGloballyPositioned
            }

            if (lastVisibility == 0f || currentTime - lastUpdateTime >= 100 && visibilityPercentage != lastVisibility) {
                lastUpdateTime = currentTime
                lastVisibility = visibilityPercentage
                onVisibilityChanged(visibilityPercentage)
            }
        }
    )
}

private fun LayoutCoordinates.getVisibilityPercentage(): Float {
    val bounds: Rect = boundsInWindow()
    val totalArea = size.width * size.height
    if (totalArea == 0) return 0f

    val visibleWidth = bounds.width.coerceAtMost(size.width.toFloat()).coerceAtLeast(0f)
    val visibleHeight = bounds.height.coerceAtMost(size.height.toFloat()).coerceAtLeast(0f)

    val visibleArea = visibleWidth * visibleHeight
    return (visibleArea / totalArea).coerceIn(0f, 1f)
}
