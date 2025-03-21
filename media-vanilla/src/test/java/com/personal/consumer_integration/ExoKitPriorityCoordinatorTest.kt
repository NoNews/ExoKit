package com.personal.consumer_integration

import com.personal.exo_kit.internal.data.coordinator.ExoKitPriorityCoordinator
import com.personal.exo_kit.internal.data.coordinator.PlaybackKey
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExoKitPriorityCoordinatorTest {

    private lateinit var coordinator: ExoKitPriorityCoordinator

    @Before
    fun setUp() {
        coordinator = ExoKitPriorityCoordinator()
    }

    @Test
    fun `first playback should be active`() {
        val firstVideoKey = PlaybackKey("video_1", "home")
        val secondVideoKey = PlaybackKey("video_2", "home")
        val thirdVideoKey = PlaybackKey("video_3", "home")

        coordinator.onPriorityChanged(firstVideoKey, 0.5f, props.position)
        coordinator.onPriorityChanged(secondVideoKey, 0.5f, props.position)
        coordinator.onPriorityChanged(thirdVideoKey, 0.5f, props.position)

        coordinator.currentActive?.let { activePlayback ->
            assert(activePlayback == firstVideoKey)
        }
    }

    @Test
    fun `second playback should be active`() {
        val firstVideoKey = PlaybackKey("video_1", "home")
        val secondVideoKey = PlaybackKey("video_2", "home")
        val thirdVideoKey = PlaybackKey("video_3", "home")


        coordinator.onPriorityChanged(firstVideoKey, 0.5f, props.position)
        coordinator.onPriorityChanged(secondVideoKey, 1f, props.position)
        coordinator.onPriorityChanged(thirdVideoKey, 0.5f, props.position)

        coordinator.currentActive?.let { activePlayback ->
            assertEquals(secondVideoKey, activePlayback)
        }
    }

    @Test
    fun `third playback should be active`() {
        val firstVideoKey = PlaybackKey("video_1", "home")
        val secondVideoKey = PlaybackKey("video_2", "home")
        val thirdVideoKey = PlaybackKey("video_3", "home")


        coordinator.onPriorityChanged(firstVideoKey, 0.5f, props.position)
        coordinator.onPriorityChanged(secondVideoKey, 0.5f, props.position)
        coordinator.onPriorityChanged(thirdVideoKey, 1f, props.position)

        coordinator.currentActive?.let { activePlayback ->
            assertEquals(thirdVideoKey, activePlayback)
        }
    }
}