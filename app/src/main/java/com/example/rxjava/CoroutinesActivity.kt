package com.example.rxjava

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.rxjava.appconstants.AppConstants
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CoroutinesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutines)

/*      createList().forEach {
            Log.d(AppConstants.TAG_COROUTINES_ACTIVITY, "onCreate:createList " + it)
        }
        createSequence().forEach {
            Log.d(AppConstants.TAG_COROUTINES_ACTIVITY, "onCreate:createSequence " + it)
        }*/
        runBlocking {
            createlistSuspend().forEach {
                Log.d(
                    AppConstants.TAG_COROUTINES_ACTIVITY,
                    "onCreate:createSuspendList " + it
                )
            }
        }
        runBlocking<Unit> {
            // Launch a concurrent coroutine to check if the main thread is blocked
            launch {
                for (k in 1..3) {
                    println("I'm not blocked $k")
                    delay(100)
                }
            }
            createFlow().collect { value -> Log.d(AppConstants.TAG_COROUTINES_ACTIVITY, "flow "+value) }
        }
    }

    private fun createList(): List<Int> {
        return listOf(1, 2, 3)
    }

    private fun createSequence(): Sequence<Int> = sequence { // sequence builder
        for (i in 1..3) {
            Thread.sleep(1000) // pretend we are computing it
            yield(i) // yield next value
        }
    }

    private suspend fun createlistSuspend(): List<Int> {
        delay(5000) // pretend we are doing something asynchronous here
        return listOf(1, 2, 3)
    }
    fun createFlow(): Flow<Int> = flow { // flow builder
        for (i in 1..3) {
            delay(100) // pretend we are doing something useful here
            emit(i) // emit next value
        }
    }

}