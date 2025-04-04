package com.personal.exokit.playground

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.personal.common.AppDi
import com.personal.consumer_integration.AutoplayMode
import com.personal.consumer_integration.MediaVanilla
import com.personal.consumer_integration.MediaVanillaProps
import com.personal.consumer_integration.SurfaceLifecycleProvider
import com.personal.exo_kit.api.data.PlayerState
import com.personal.exo_kit.api.extensions.extractDecodersInfo
import com.personal.exo_kit.api.ui.rememberVideoPlaybackState
import com.personal.exo_kit.api.ui.rememberWishes
import com.personal.exo_kit.internal.data.store.wish.ConsumerWish
import com.personal.exokit.data.VideoRepository
import com.personal.exokit.ui.theme.ExokitTheme

class PlaygroundActivity : ComponentActivity() {
    @SuppressLint("UnsafeOptInUsageError")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ExokitTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    VideoPlayground(Modifier.padding(innerPadding))
                }
            }
        }
    }


    @Composable
    private fun VideoPlayground(modifier: Modifier) {
        val video = VideoRepository().getVideos().first()

        SurfaceLifecycleProvider {

            Column(modifier) {
                MediaVanilla(
                    props = MediaVanillaProps(
                        mediaId = video.mediaId,
                        videoUrl = video.url,
                        width = video.width,
                        height = video.height,
                        thumbnailUrl = video.thumbnail,
                        surfaceName = "playground_screen",
                        loop = false,
                        position = 0,
                        scale = ContentScale.FillWidth,
                        autoplayMode = AutoplayMode.APP_SETTINGS
                    ),
                    modifier = Modifier.height(300.dp)
                        .fillMaxWidth()
                )
                RetryBlock(mediaId = video.mediaId)
            }
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    @Composable
    private fun RetryBlock(mediaId: String) {
        val playbackState = rememberVideoPlaybackState(mediaId)
        val codecsSelector = AppDi.codecsSelector

        val error = playbackState?.playerState as? PlayerState.Error
        val brokenCodec = error?.exception?.extractDecodersInfo()?.playbackDecoder


        val wishes = rememberWishes()

        Button(
            onClick = {
                if (brokenCodec != null) {
                    codecsSelector.markAsDirty(brokenCodec.name)
                    wishes.wish(
                        mediaId = mediaId,
                        wish = ConsumerWish.Retry
                    )
                }

            }
        ) {
            Text("Retry/Play again")
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, PlaygroundActivity::class.java).apply {
            }
            context.startActivity(intent)
        }

        const val EXTRAS_SEED_MEDIA_ID = "extras_seed_media_id"
    }
}
