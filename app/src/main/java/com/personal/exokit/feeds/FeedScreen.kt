package com.personal.exokit.feeds

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.exoplayer.ExoPlayer
import com.personal.common.AppDi
import com.personal.exo_kit.internal.data.ExoKitPlayer
import com.personal.exokit.MainActivity
import com.personal.exokit.data.PostRepository
import com.personal.exokit.data.VideoRepository


@ExperimentalMaterial3Api
@Composable
fun FeedScreen(modifier: Modifier) {

    val viewModel = remember {
        FeedViewModel(
            VideoRepository(),
            PostRepository()
        )
    }
    val state by viewModel.state


    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    val debugModeEnabled by
                    AppDi.appSettingsRepository.observeDebugMode().collectAsState()
                    RoundIconButton(
                        imageVector = Icons.Default.Build,
                        contentDescription = "Debug",
                        modifier = Modifier,
                        tint = if (debugModeEnabled) Color.Blue else MaterialTheme.colorScheme.onSurface,
                        onClick = {
                            AppDi.appSettingsRepository.toggleDebug()
                        }
                    )
//                    RoundIconButton(
//                        imageVector = Icons.Default.Settings,
//                        contentDescription = "Settings",
//                        modifier = Modifier,
//                        onClick = {
//
//                        }
//                    )
//                    RoundIconButton(
//                        imageVector = Icons.Default.Search,
//                        contentDescription = "Debug",
//                        modifier = Modifier,
//                        onClick = {
//
//                        }
//                    )
//                    val context = LocalContext.current
//                    RoundIconButton(
//                        imageVector = Icons.Filled.Place,
//                        contentDescription = "Playground",
//                        modifier = Modifier,
//                        onClick = {
//                            PlaygroundActivity.start(context)
//                        }
//                    )
                },
                title = {
                    Text("Feeds")
                }
            )
        },
    ) { innerPadding ->
        FeedList(innerPadding, viewModel)
    }

}

@Composable
private fun FeedList(
    innerPadding: PaddingValues,
    viewModel: FeedViewModel,
) {
    val state by viewModel.state
    val sortOption = state.sortOption
    val elements = state.items
    Column(
        modifier = Modifier.padding(
            top = innerPadding.calculateTopPadding(),
            start = 0.dp,
            end = 0.dp,
            bottom = 0.dp
        )
    ) {


        SorterComponent(
            selectedSort = sortOption,
            onSortSelected = { selected -> viewModel.onSortSelected(selected) },
            modifier = Modifier.padding(start = 16.dp),
        )


//        val debugEnabled by AppDi.appSettingsRepository.observeDebugMode().collectAsState()
//        if (debugEnabled) {
//            PlayerPoolDebug()
//        }
        LazyColumn(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 24.dp)) {
            items(elements, key = { element -> element.hashCode() }) { element ->
                when (element) {
                    is FeedUiModel.Video -> {
                        VideoPost(
                            element = element
                        )
                    }

                    is FeedUiModel.Text -> {
                        TextPost(element)
                    }
                }
            }
        }
    }
}

@SuppressLint("MutableCollectionMutableState")
@Composable
private fun PlayerPoolDebug() {
    val activity = (LocalContext.current as Activity as MainActivity)
    var storedState by remember() { mutableStateOf(LinkedHashMap<String, ExoKitPlayer>()) }

    // Register listener to update state
    LaunchedEffect(activity) {
        AppDi.playerPool.addAssociationListener { state ->
            storedState =
                LinkedHashMap(state) // Ensure a new instance is set to trigger recomposition
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Player Pool State", fontWeight = FontWeight.Bold, fontSize = 18.sp)

        if (storedState.isEmpty()) {
            Text("No players in the pool.", fontStyle = FontStyle.Italic)
        } else {
            storedState.forEach { (postId, player) ->
                Text(
                    text = "${player.name} -> $postId, state: ${
                        player.exo.playbackState.toExoState(
                            player.exo
                        )
                    }",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}


// Convert ExoPlayer playback states into human-readable text
private fun Int.toExoState(exo: ExoPlayer): String {
    return when (this) {
        ExoPlayer.STATE_IDLE -> "Idle"
        ExoPlayer.STATE_BUFFERING -> "Buffering"
        ExoPlayer.STATE_READY -> {
            if (exo.playWhenReady || exo.isPlaying) {
                "Playing"
            } else {
                "Paused"
            }
        }

        ExoPlayer.STATE_ENDED -> "Ended"
        else -> "Unknown"
    }
}


@Composable
fun RoundIconButton(
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.onSurface
) {

    Box(
        contentAlignment = Alignment.Center, // Ensures Icon stays centered
        modifier = modifier
            .size(48.dp) // Adjust the touch target size
            .clip(CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false, radius = 24.dp)
            ) {
                onClick()
            }
            .background(MaterialTheme.colorScheme.surfaceVariant) // Optional background
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(24.dp) // Ensure the icon fits well inside
        )
    }
}


@Composable
private fun TextPost(element: FeedUiModel.Text) {
    Column(modifier = Modifier.padding(top = 16.dp)) {
        Text(
            text = element.title,
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center
        )

    }
    Text(
        text = element.text,
        style = TextStyle(fontSize = 16.sp),
    )
}


enum class SortOption(val label: String) {
    VODEO_AND_POSTS_STABLE("Videos + Posts (stable)"),
    VODEO_AND_POSTS_SHUFFLED("Videos + Posts (shuffled)"),
    VIDEOS_ONLY_SHUFFLED("Videos only (shuffled)"),
    VIDEOS_ONLY_STABLE("Videos only (stable)"),
}

@Composable
fun SorterComponent(
    selectedSort: SortOption,
    onSortSelected: (SortOption) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        // Clickable text to open dropdown
        Row(
            modifier = Modifier
                .clickable { expanded = true }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedSort.label,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Expand Sort Options",
                modifier = Modifier.size(20.dp)
            )
        }

        // Dropdown menu with sorting options
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            SortOption.entries.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.label) },
                    onClick = {
                        onSortSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}


