package com.personal.consumer_integration.blocks

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.personal.exo_kit.api.data.PlayerState
import com.personal.exo_kit.api.ui.rememberVideoPlaybackState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeekbarBlock(mediaId: String, modifier: Modifier = Modifier) {
    val state = rememberVideoPlaybackState(mediaId)
    val isPlaying = state.playerState is PlayerState.Playing
    val lastKnownPosition = state.position ?: 0
    val duration = state.duration ?: 0
    val playbackSpeed = state.playbackSpeed
    val lastUpdate = state.snapshotTimestamp

    val progressFraction =
        if (duration > 0) lastKnownPosition.toFloat() / duration.toFloat() else 0f

    val progressAnimation = remember { Animatable(progressFraction) }

    var sliderPosition by remember { mutableFloatStateOf(0f) }
    val remainingDuration = (duration - lastKnownPosition) / playbackSpeed


    LaunchedEffect(isPlaying, lastKnownPosition) {
        if (isPlaying) {
            progressAnimation.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = remainingDuration.toInt(),
                    easing = LinearEasing
                )
            )
        } else {
            progressAnimation.stop()
        }
    }

    val progressVale by progressAnimation.asState()


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Selected Value: ${sliderPosition.toInt()}")

        val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
        val colors: SliderColors = SliderDefaults.colors()
        Slider(
            value = progressVale,
            onValueChange = { sliderPosition = it },
            colors = colors,
            interactionSource = interactionSource,
            valueRange = 0f..1.0f,
            onValueChangeFinished = {
                // This block is called when the user stops interacting with the slider
            },
            thumb = {
                SliderDefaults.Thumb(
                    interactionSource = interactionSource,
                    colors = colors,
                    enabled = true,
                )
            },
            modifier = Modifier.padding(vertical = 16.dp),
        )
    }
}