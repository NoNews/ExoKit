package com.personal.exokit.feeds

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.consumer_integration.AutoplayMode
import com.personal.consumer_integration.Controls
import com.personal.consumer_integration.MediaVanillaProps
import com.personal.exokit.data.PostRepository
import com.personal.exokit.data.VideoRepository
import kotlinx.coroutines.launch

@Stable
class FeedViewModel(
    private val videoRepository: VideoRepository,
    private val postRepository: PostRepository,
) : ViewModel() {

    val state = mutableStateOf(
        FeedUiState(items = emptyList(), sortOption = SortOption.VIDEOS_ONLY_SHUFFLED)
    )

    init {
        renderState()
    }

    private fun renderState() {
        viewModelScope.launch {
            onSortSelected(SortOption.VIDEOS_ONLY_SHUFFLED)
        }
    }

    fun onSortSelected(selected: SortOption) {
        viewModelScope.launch {
            val videos = getMappedVideos()
            val posts = getMappedPosts()

            val sortedItems = when (selected) {
                SortOption.VIDEOS_ONLY_STABLE -> videos
                SortOption.VIDEOS_ONLY_SHUFFLED -> videos.shuffled()
                SortOption.VODEO_AND_POSTS_STABLE -> videos + posts
                SortOption.VODEO_AND_POSTS_SHUFFLED -> (videos + posts).shuffled()
            }.reindexPositions()

            // Update state with sorted items and new selection
            state.value = state.value.copy(items = sortedItems, sortOption = selected)
        }
    }

    private suspend fun getMappedVideos(): List<FeedUiModel.Video> {
        return videoRepository.getVideos().mapIndexed { index, video ->
            FeedUiModel.Video(
                title = video.title,
                props = MediaVanillaProps(
                    mediaId = video.mediaId,
                    videoUrl = video.url,
                    thumbnailUrl = video.thumbnail,
                    width = video.width,
                    height = video.height,
                    controls = Controls.Default,
                    surfaceName = "home_feed",
                    loop = false,
                    scale = ContentScale.FillBounds,
                    autoplayMode = AutoplayMode.APP_SETTINGS,
                    position = index
                )
            )
        }
    }

    private suspend fun getMappedPosts(): List<FeedUiModel.Text> {
        return postRepository.getPosts().map { post ->
            FeedUiModel.Text(
                title = post.title,
                text = post.text
            )
        }
    }

    private fun List<FeedUiModel>.reindexPositions(): List<FeedUiModel> {
        return mapIndexed { index, item ->
            if (item is FeedUiModel.Video) {
                item.copy(props = item.props.copy(position = index))
            } else {
                item
            }
        }
    }
}


@Immutable
data class FeedUiState(
    val items: List<FeedUiModel>,
    val sortOption: SortOption,
)

@Immutable
sealed class FeedUiModel {

    @Immutable
    data class Video(val title: String, val props: MediaVanillaProps) : FeedUiModel()

    @Immutable
    data class Text(val title: String, val text: String) : FeedUiModel()
}