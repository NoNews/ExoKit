package com.personal.exo_kit.internal.data.coordinator


import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ExoKitPriorityCoordinator {
    private val _currentActive = MutableStateFlow<PlaybackKey?>(null)
    val currentActive: StateFlow<PlaybackKey?> get() = _currentActive

    private val visibilities = mutableMapOf<PlaybackKey, PlaybackVisibility>()

    fun onPriorityChanged(playbackKey: PlaybackKey, visibility: Float, position: Int) {
        val visibilityPercent = (visibility * 100).toInt()
        visibilities[playbackKey] = PlaybackVisibility(visibilityPercent, position)

        val allEligible = visibilities
            .filterValues { it.videoVisibilityFraction >= 50 } // Now comparing percentages
            .map { it.key to it.value } // Convert Map to List<Pair<PlaybackKey, PlaybackVisibility>>
            .sortedWith(
                compareByDescending<Pair<PlaybackKey, PlaybackVisibility>> { it.second.videoVisibilityFraction }
                    .thenBy { it.second.position } // Lower position gets priority in case of equal visibility
            )

        val eligibleToPlayKey = allEligible.firstOrNull()?.first
        _currentActive.value = eligibleToPlayKey

        // Logging
        val logBuilder = StringBuilder()
        logBuilder.append("\n========================================\n")

        eligibleToPlayKey?.let { activeKey ->
            val activeVisibility = visibilities[activeKey]
            logBuilder.append("active: ${activeKey.mediaId}, ${activeVisibility?.videoVisibilityFraction ?: 0}%, ${activeVisibility?.position}\n")
        } ?: logBuilder.append("active: NONE\n")

        logBuilder.append("others:\n")
        allEligible.drop(1).forEach { (key, visibility) ->
            logBuilder.append("         ${key.mediaId}, ${visibility.videoVisibilityFraction}%, ${visibility.position}\n")
        }

        Log.d("ExoKitPriority", logBuilder.toString())
    }


    fun remove(key: PlaybackKey) {
        visibilities.remove(key)
    }
}

data class PlaybackVisibility(
    val videoVisibilityFraction: Int,
    val position: Int,
)

data class PlaybackKey(
    val mediaId: String,
    val surfaceId: String,
)
