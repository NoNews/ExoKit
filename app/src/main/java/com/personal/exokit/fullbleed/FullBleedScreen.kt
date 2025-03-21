package com.personal.exokit.fullbleed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.personal.exokit.data.VideoRepository

@Composable
fun FullBleedScreen(seedMediaId: String) {
    val viewModel = remember {
        FullBleedViewModel(
            videoRepository = VideoRepository(),
            seedMediaId = seedMediaId,
        )
    }

    val items by viewModel.state

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // HorizontalPager to swipe videos horizontally
        val state = rememberPagerState {
            items.size
        }

        HorizontalPager(
            state = state,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            FullBleedVideoPost(
                props = items[page].props
            )
        }
    }
}