package com.personal.exo_kit.internal


import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

interface MapSharedFlow<K, V> {
    fun set(key: K, value: V)
    fun observe(key: K): Flow<V> // Change return type to Flow<V>
}

fun <K, V> mutableSharedMapFlow(
    replay: Int = 10,
    extraBufferCapacity: Int = 10,
    onBufferOverflow: BufferOverflow = BufferOverflow.DROP_OLDEST,
): MapSharedFlow<K, V> {
    val stateMap = mutableMapOf<K, MutableSharedFlow<V>>()

    return object : MapSharedFlow<K, V> {
        private fun getOrCreate(key: K): MutableSharedFlow<V> {
            return stateMap.getOrPut(key) {
                MutableSharedFlow(replay, extraBufferCapacity, onBufferOverflow)
            }
        }

        override fun set(key: K, value: V) {
            getOrCreate(key).tryEmit(value)
        }

        override fun observe(key: K): Flow<V> {
            return getOrCreate(key)
        }
    }
}