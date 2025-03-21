package com.personal.consumer_integration.blocks

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.exoplayer.upstream.DefaultBandwidthMeter
import com.personal.common.AppDi
import com.personal.exo_kit.api.data.ActiveVideo
import com.personal.exo_kit.api.ui.rememberActiveVideoMediator
import com.personal.exo_kit.api.ui.rememberVideoPlaybackState
import java.util.Locale

@SuppressLint("UnsafeOptInUsageError")
@Composable
fun DebugBlock(
    mediaId: String,
    modifier: Modifier = Modifier,
    videoVisibilityFraction: Float
) {

    val debugEnabled by AppDi.appSettingsRepository.observeDebugMode().collectAsState()

    if (!debugEnabled) {
        return
    }
    val state = rememberVideoPlaybackState(mediaId)

    val activeVideo by rememberActiveVideoMediator().observeActiveVideo().collectAsState()

    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    val activeVideoId = (activeVideo as? ActiveVideo.Known)?.mediaId
    val activeLabel = if (activeVideoId == mediaId) "✅" else "⛔"

    val debugInfo = """
        mediaId: $mediaId
        visibility: $videoVisibilityFraction
        active: $activeLabel 
        state: ${state.playerState}
        player: ${state.playerName}     
    """.trimIndent()

    Column(
        modifier = modifier
            .background(Color.Black.copy(alpha = 0.7f)) // Dark semi-transparent background
            .padding(8.dp) // Padding for text visibility
            .clickable {
                clipboardManager.setText(AnnotatedString(debugInfo))
                Toast.makeText(context, "Debug info copied!", Toast.LENGTH_SHORT).show()
            }
    ) {
        val bandwidthMeter = DefaultBandwidthMeter.getSingletonInstance(context)
        val bitrateMbps = bandwidthMeter.bitrateEstimate / 1_000_000.0
        val formattedBitrate = String.format(Locale.US, "%.2f Mbps", bitrateMbps)

        DebugRow(title = "mediaId", value = mediaId)
        DebugRow(title = "player", value = state.playerName.toString())
        DebugRow(title = "visibility", value = videoVisibilityFraction.toString())
        DebugRow(title = "active", value = activeLabel)
        DebugRow(title = "state", value = state.playerState.toString())
        DebugRow(title = "network", value = formattedBitrate)
    }
}

@Composable
private fun DebugRow(title: String, value: String) {
    Row {
        Text(
            text = "$title:",
            color = Color.White, // White text
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = value,
            color = Color.White, // White text
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal
        )
    }
}