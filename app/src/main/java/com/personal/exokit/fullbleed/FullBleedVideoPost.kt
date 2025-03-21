package com.personal.exokit.fullbleed

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.personal.consumer_integration.MediaVanilla
import com.personal.consumer_integration.MediaVanillaProps
import com.personal.consumer_integration.SurfaceLifecycleProvider

@Composable
fun FullBleedVideoPost(props: MediaVanillaProps) {
    SurfaceLifecycleProvider {
        MediaVanilla(
            props = props,
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding()
        )
    }
}