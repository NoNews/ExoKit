package com.personal.exokit.fullbleed

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.consumer_integration.Controls
import com.personal.consumer_integration.MediaVanillaProps
import com.personal.exokit.data.VideoRepository
import kotlinx.coroutines.launch

@Stable
class FullBleedViewModel(
    private val videoRepository: VideoRepository,
    private val seedMediaId: String,
) : ViewModel() {

    val state = mutableStateOf(emptyList<FullBleedUiModel>())

    init {
        viewModelScope.launch {
            val items = videoRepository.getVideos()
                .mapIndexed { index, video ->
                    FullBleedUiModel(
                        MediaVanillaProps(
                            mediaId = video.mediaId,
                            videoUrl = video.url,
                            thumbnailUrl = video.thumbnail,
                            width = video.width,
                            height = video.height,
                            controls = Controls.Default,
                            surfaceName = "fbp",
                            loop = true,
                            scale = ContentScale.FillWidth,
                            position = index
                        )
                    )
                }
                .sortedByDescending { it.props.mediaId == seedMediaId }

            state.value = items
        }
    }
}


@Immutable
data class FullBleedUiModel(val props: MediaVanillaProps)