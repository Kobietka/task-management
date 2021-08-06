package com.kobietka.taskmanagement.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.ActivityNavigatorExtras
import com.kobietka.taskmanagement.data.TaskSessionEntity
import com.kobietka.taskmanagement.repository.inter.TaskSessionRepository
import com.kobietka.taskmanagement.ui.util.Timer
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TimeMeasureViewModel
@Inject constructor(private val taskSessionRepository: TaskSessionRepository): ViewModel() {

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

    fun resetTimer(){
        _timeText.value = convertTime(0)
        timer.stop()
    }

    fun pauseTimer(){
        timer.stop()
    }

    fun saveSession(taskId: Int, timeInSeconds: Int){
        val calendar = Calendar.getInstance()
        val currentDate = "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH)}/${calendar.get(Calendar.YEAR)}"

        taskSessionRepository.insert(
            TaskSessionEntity(
                id = null,
                timeInSeconds = timeInSeconds,
                date = currentDate,
                taskId = taskId
            )
        ).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun loadSessions(taskId: Int, onFinish: (List<TaskSessionEntity>) -> Unit){
        taskSessionRepository.getAllByTaskId(taskId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(onFinish)
    }

    fun convertTime(seconds: Int): String {
        val minutes = seconds/60
        val secondsModulo = seconds%60
        var minutesText = ""
        var secondsText = ""

        minutesText = if(minutes < 10) "0$minutes" else "$minutes"
        secondsText = if(secondsModulo < 10) "0$secondsModulo" else "$secondsModulo"

        return "$minutesText:$secondsText"
    }

}