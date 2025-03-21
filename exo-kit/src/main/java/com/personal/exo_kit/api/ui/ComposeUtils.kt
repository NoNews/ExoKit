package com.personal.exo_kit.api.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.personal.exo_kit.api.data.ActiveVideoMediator
import com.personal.exo_kit.api.data.ExoKitPlaybackStore
import com.personal.exo_kit.api.data.PlaybackState
import com.personal.exo_kit.internal.data.DI
import com.personal.exo_kit.internal.data.store.wish.ConsumerWishes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull

@Composable
fun rememberVideoPlaybackStore(): ExoKitPlaybackStore {
    return remember {
        DI.playbackStore
    }
}

@Composable
fun rememberWishes(): ConsumerWishes {
    return remember { DI.consumerWishes }
}

@Composable
fun rememberActiveVideoMediator(): ActiveVideoMediator {
    return remember {
        DI.activeVideoMediator
    }
}

@Composable
fun rememberVideoPlaybackState(mediaId: String): PlaybackState {
    val store = rememberVideoPlaybackStore()
    val initialState = remember(mediaId) {
        store.state.value.playbacks.getOrDefault(mediaId, PlaybackState())
    }
    val state by remember(mediaId) {
        store.state.mapNotNull {
            it.playbacks.getOrDefault(mediaId, PlaybackState())
        }.distinctUntilChanged()
    }.collectAsState(
        initial = initialState,
        context = Dispatchers.Default
    )
    return state
}