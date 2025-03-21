package com.personal.exokit.fullbleed

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

class FullBleedActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // get args here
        val seedMediaId = intent.extras?.getString(EXTRAS_SEED_MEDIA_ID) ?: ""
        setContent {
            FullBleedScreen(seedMediaId)
        }
    }

    companion object {
        fun start(context: Context, seedMediaId: String) {
            val intent = Intent(context, FullBleedActivity::class.java).apply {
                putExtra(EXTRAS_SEED_MEDIA_ID, seedMediaId)
            }
            context.startActivity(intent)
        }

        const val EXTRAS_SEED_MEDIA_ID = "extras_seed_media_id"
    }
}