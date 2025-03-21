package com.personal.consumer_integration.blocks

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeMute
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.personal.consumer_integration.di.rememberMediaSettings
import com.personal.exo_kit.api.data.ActiveVideo
import com.personal.exo_kit.api.data.AudioTrackState
import com.personal.exo_kit.api.ui.rememberActiveVideoMediator
import com.personal.exo_kit.api.ui.rememberVideoPlaybackState
import kotlinx.coroutines.flow.filter

@Composable
fun MuteButtonBlock(
    modifier: Modifier,
    mediaId: String
) {

    var drawn by remember { mutableStateOf(false) }
    val mediaSettingsStore = rememberMediaSettings()
    val activeVideoMediator = rememberActiveVideoMediator()

    val audioTrack = rememberVideoPlaybackState(mediaId).audio

    val activeVideo by activeVideoMediator.observeActiveVideo()
        .collectAsState()

    val activeVideoId = (activeVideo as? ActiveVideo.Known)?.mediaId
    val videoActive = activeVideoId == mediaId

    val sessionMutedByUser by mediaSettingsStore.observeMuteSession()
        .filter { videoActive }
        .collectAsState(mediaSettingsStore.sessionMuted())


    val (icon, clickable) = when {
        audioTrack == AudioTrackState.HAS_SOUND && sessionMutedByUser -> Icons.AutoMirrored.Filled.VolumeOff to videoActive
        audioTrack == AudioTrackState.HAS_SOUND && !sessionMutedByUser -> Icons.AutoMirrored.Filled.VolumeMute to videoActive
        audioTrack == AudioTrackState.HAS_NO_SOUND && sessionMutedByUser -> Icons.AutoMirrored.Filled.VolumeOff to videoActive
        audioTrack == AudioTrackState.HAS_NO_SOUND && !sessionMutedByUser -> Icons.AutoMirrored.Filled.VolumeMute to videoActive
        audioTrack == AudioTrackState.UNKNOWN && sessionMutedByUser -> Icons.AutoMirrored.Filled.VolumeOff to videoActive
        audioTrack == AudioTrackState.UNKNOWN && !sessionMutedByUser -> Icons.AutoMirrored.Filled.VolumeMute to videoActive
        else -> /*error("KEK $audioTrack, $sessionMutedByUser")*/ return
    }

    Box(modifier = modifier) {
        Image(
            imageVector = icon,
            colorFilter = ColorFilter.tint(Color.White),
            contentDescription = if (sessionMutedByUser) {
                "video mute"
            } else {
                "video unmute"
            },
            modifier = Modifier
                .clip(shape = RoundedCornerShape(24.dp))
                .clickable(enabled = clickable) {
                    mediaSettingsStore.setMuted(!sessionMutedByUser)
                }
                .padding(8.dp)
                .background(
                    color = Color.Black.copy(alpha = .5f),
                    shape = RoundedCornerShape(24.dp)
                )
                .testTag("post_media_audio_toggle"),
        )
        drawn = true
    }
}
