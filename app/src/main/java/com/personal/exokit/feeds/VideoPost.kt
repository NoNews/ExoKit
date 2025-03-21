package com.personal.exokit.feeds

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personal.consumer_integration.MediaVanilla
import com.personal.consumer_integration.SurfaceLifecycleProvider
import com.personal.exokit.fullbleed.FullBleedActivity
import kotlin.math.min


@Composable
fun VideoPost(
    element: FeedUiModel.Video,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current




    Column(modifier = modifier.padding(top = 16.dp)
        .clickable {
            FullBleedActivity.start(context, element.props.mediaId)
        }) {
        Text(
            text = element.title,
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 4.dp)
        )


        val screenSize = screenSize()
        val videoHeightDp = calculateMediaCropHeight(
            containerSize = screenSize,
            aspectRatio = element.props.width.toFloat() / element.props.height.toFloat()
        )

        SurfaceLifecycleProvider {
            MediaVanilla(
                props = element.props,
                modifier = Modifier.fillMaxWidth()
                    .height(videoHeightDp)
                    .clip(RoundedCornerShape(16.dp))
            )
        }
    }
}

@Composable
private fun calculateMediaCropHeight(containerSize: Size, aspectRatio: Float): Dp {
    val width = with(LocalDensity.current) { containerSize.width - 32.dp.roundToPx() }
    val height: Int = (width / aspectRatio).toInt()
    val maxHeightLimit = width * 4 / 3

    val heightDp = with(LocalDensity.current) {
        min(height.toFloat(), maxHeightLimit).toDp()
    }
    return heightDp
}

@Composable
fun screenSize(): Size {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    return remember(configuration, density) {
        val (width, height) = with(density) {
            configuration.screenWidthDp.dp.toPx() to configuration.screenHeightDp.dp.toPx()
        }

        Size(width, height)
    }
}