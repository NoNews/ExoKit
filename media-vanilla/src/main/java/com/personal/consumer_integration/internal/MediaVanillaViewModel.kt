package com.personal.consumer_integration.internal

import android.annotation.SuppressLint
import com.personal.consumer_integration.AutoplayMode
import com.personal.consumer_integration.MediaVanillaProps
import com.personal.consumer_integration.settings.MediaSessionStore
import com.personal.exo_kit.api.data.ActiveVideoMediator
import com.personal.exo_kit.api.data.ExoKitPlaybackStore
import com.personal.exo_kit.api.data.PlayerEffect
import com.personal.exo_kit.api.ui.params.SurfaceLifecycle
import com.personal.exo_kit.internal.data.store.wish.ConsumerWish
import com.personal.exo_kit.internal.data.store.wish.ExoKitWishes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("UnsafeOptInUsageError")
class MediaVanillaViewModel(
    private val playbackStore: ExoKitPlaybackStore,
    private val props: MediaVanillaProps,
    private val mediaSettingsStore: MediaSessionStore,
    private val activeVideoMediator: ActiveVideoMediator,
    private val scope: CoroutineScope,
    private val exoKitWishes: ExoKitWishes,
) {
    init {
        scope.launch {
            mediaSettingsStore.observeMuteSession()
                .collectLatest { mutedByUser ->
                    exoKitWishes.wish(
                        mediaId = props.mediaId,
                        wish = ConsumerWish.ToggleMute(mute = mutedByUser)
                    )
                }
        }

        scope.launch {
            playbackStore.observeEffects().collectLatest { effect ->
                when (effect) {
                    is PlayerEffect.VideoRestarted -> {
                        // analytics that video is restarted
                    }
                }
            }
        }

        when (props.autoplayMode) {
            AutoplayMode.APP_SETTINGS -> {
                scope.launch {
                    mediaSettingsStore.observeAutoplaySession()
                        .collectLatest { autoplay ->
                            exoKitWishes.wish(
                                mediaId = props.mediaId,
                                wish = ConsumerWish.ToggleAutoplay(autoplay)
                            )
                        }
                }
            }

            AutoplayMode.ENABLED -> {
                exoKitWishes.wish(
                    mediaId = props.mediaId,
                    wish = ConsumerWish.ToggleAutoplay(true)
                )
            }

            AutoplayMode.DISABLED -> {
                exoKitWishes.wish(
                    mediaId = props.mediaId,
                    wish = ConsumerWish.ToggleAutoplay(false)
                )
            }

            AutoplayMode.BLURRED -> {
                exoKitWishes.wish(
                    mediaId = props.mediaId,
                    wish = ConsumerWish.ToggleAutoplay(false)
                )
            }
        }
    }

    fun onLifecycleChanged(videoVisibilityFraction: Float, lifecycle: SurfaceLifecycle) {
        exoKitWishes.wish(
            mediaId = props.mediaId,
            wish = ConsumerWish.PriorityChanged(
                fraction = videoVisibilityFraction,
            ),
        )
    }

    fun onCleared() {
        scope.coroutineContext.cancelChildren()
    }
}