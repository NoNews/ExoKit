@file:OptIn(ExperimentalMaterial3Api::class)

package com.personal.exokit

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.personal.exokit.feeds.FeedScreen
import com.personal.common.AppDi
import com.personal.exo_kit.api.ExoKit
import com.personal.exokit.ui.theme.ExokitTheme

@SuppressLint("UnsafeOptInUsageError")
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)


    @SuppressLint("UnsafeOptInUsageError")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        AppDi.initialise(applicationContext)
        AppDi.playerPool.prewarm()

        ExoKit.initialise(
            context = applicationContext,
            poolProvider = {
                AppDi.playerPool
            },
            preloadManagerProvider = {
                AppDi.preloadManagerBuilder.build()
            }
        )

        setContent {
            ExokitTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FeedScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}