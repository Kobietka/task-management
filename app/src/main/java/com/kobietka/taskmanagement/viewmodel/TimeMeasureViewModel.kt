package com.kobietka.taskmanagement.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kobietka.taskmanagement.ui.util.Timer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TimeMeasureViewModel
@Inject constructor(): ViewModel() {

    private val _timeText = MutableLiveData("00:00")
    private val _time = MutableLiveData(0)
    private val timer = Timer().onTick { seconds ->
        _timeText.value = convertTime(seconds)
        _time.value = seconds
    }

    fun timeText(): LiveData<String> {
        return _timeText
    }

    fun time(): LiveData<Int> {
        return _time
    }

    fun startTimer(){
        timer.start()
    }

    fun pauseTimer(){
        timer.stop()
    }

    private fun convertTime(seconds: Int): String {
        val minutes = seconds/60
        val secondsModulo = seconds%60
        var minutesText = ""
        var secondsText = ""

        minutesText = if(minutes < 10) "0$minutes" else "$minutes"
        secondsText = if(secondsModulo < 10) "0$secondsModulo" else "$secondsModulo"

        return "$minutesText:$secondsText"
    }

}