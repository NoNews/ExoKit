package com.personal.exo_kit


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class SharedFlowTest {

    @Test
    fun `shared flow emits and collects correctly`() = runBlocking {
        // Given: A SharedFlow that allows buffering the last emitted value
        val sharedFlow = MutableSharedFlow<Int>(replay = 1)

        // When: Emit a value
        sharedFlow.emit(2 + 2)

        // Then: Collect explicitly and check
        var collectedValue: Int? = null
        val job = launch {
            sharedFlow.collect {
                collectedValue = it
                cancel() // Stop collecting after the first value
            }
        }



        job.join() // Wait for the collection to complete
        assertEquals(4, collectedValue)
    }
}

