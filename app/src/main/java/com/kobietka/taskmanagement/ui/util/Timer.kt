package com.kobietka.taskmanagement.ui.util

import kotlinx.coroutines.*
import kotlin.concurrent.timer


class Timer {

    private lateinit var tick: (Int) -> Unit
    private var timerRunning = false

    fun start(): Timer {
        timerRunning = true
        var timePassed = 0
        CoroutineScope(Dispatchers.IO).launch {
            while(timerRunning){
                delay(1000)
                timePassed++
                if(timerRunning){
                    withContext(Dispatchers.Main){
                        tick(timePassed)
                    }
                }
            }
        }
        return this
    }

    fun stop(){
        timerRunning = false
    }

    fun onTick(tick: (Int) -> Unit): Timer {
        this.tick = tick
        return this
    }

}