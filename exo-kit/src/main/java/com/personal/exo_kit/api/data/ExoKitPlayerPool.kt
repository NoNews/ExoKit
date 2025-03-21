package com.personal.exo_kit.api.data

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import com.personal.exo_kit.GlobalLogger
import com.personal.exo_kit.internal.data.ExoKitPlayer
import com.personal.exo_kit.internal.data.PlayerName
import java.util.LinkedList

class ExoKitPlayerPool(
    val context: Context,
    val poolSize: () -> Int = { 3 }, // TODO: keep it simple, int should be enough ??
                                     // TODO: you should set some limitation there, you should not live it open like that
                                     // TODO: how do i know how many players to use?
                                     // In long form content, a video that has a design preview instead of the first frame
                                     // immediately starting the video is negative, accoridng to some researches that AHmed did in Spotify
                                     // especially for a long form, where you committing a lot of time as a watcher. The design image is used to inform you of what to expect in the video.
                                     // and if you don't have enough time to read what is it, you;re more likely to drop the video.
                                     // so we need to distinguish video which has "hero" image and "not".
    val playerCreator: (Context) -> ExoPlayer = { context -> ExoPlayer.Builder(context).build() }
) {


    val playerPool = LinkedHashMap<String, ExoKitPlayer>(
        /*initialCapacity =*/ poolSize.invoke(),
        /*loadFactor =*/ 0.75f,
        /*accessOrder =*/ false,
    )

    private val players = LinkedList<ExoKitPlayer>()

    private val listeners = mutableListOf<AssociationListener>()

    fun addAssociationListener(listener: AssociationListener) {
        listeners += listener
    }

    fun removeAssociationListener(listener: AssociationListener) {
        listeners -= listener
    }

    fun prewarm() {
        PlayerName.entries.forEachIndexed { i, name ->
            playerPool += "$CLEAN_PLAYER#$i" to createPlayer(name)
        }
    }

    fun pauseAllBut(key: String) {
        playerPool.forEach { (k, v) ->
            if (k != key) {
                v.exo.playWhenReady = false
            }
        }
        notifyListenersForDebugState()
    }

    fun cached(key: String): ExoKitPlayer? {
        playerPool[key]?.let {
            notifyListenersForDebugState()
            GlobalLogger.log("STATE POOL: BEST, CACHED PLAYER: $key")
            return it
        }
        notifyListenersForDebugState()
        return null
    }

    fun acquire(key: String, allowDirty: Boolean = false): ExoKitPlayer? {
        playerPool[key]?.let {
            notifyListenersForDebugState()
            GlobalLogger.log("STATE POOL: BEST, CACHED PLAYER: $key")
            return it
        }

        val reusableKey = playerPool.keys.firstOrNull { it.startsWith(CLEAN_PLAYER) }
            ?: playerPool.filter { !it.value.isDirty() }.keys.lastOrNull()

        reusableKey?.let {
            val player = playerPool.remove(it)!!
            playerPool[key] = player

            if (player.exo.playbackState != ExoPlayer.STATE_IDLE) {
                player.exo.stop()
            }

            GlobalLogger.log("STATE POOL: REUSED PLAYER: $key, reassociated from $it")
            notifyListenersForDebugState()
            return player
        }

        notifyListenersForDebugState()
        return null
    }

    private fun notifyListenersForDebugState() {
        listeners.forEach { it.onAssociationChanged(playerPool) }
    }

    private fun createPlayer(name: PlayerName): ExoKitPlayer = ExoKitPlayer(
        exo = playerCreator(context),
        name = name
    )

    companion object {
        const val CLEAN_PLAYER = "clean_player"
    }
}

fun interface AssociationListener {
    fun onAssociationChanged(association: LinkedHashMap<String, ExoKitPlayer>)
}