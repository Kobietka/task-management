package com.kobietka.taskmanagement.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kobietka.taskmanagement.data.entity.TaskSessionEntity
import com.kobietka.taskmanagement.domain.usecase.task.LoadTask
import com.kobietka.taskmanagement.domain.usecase.tasksession.LoadSessions
import com.kobietka.taskmanagement.domain.usecase.tasksession.SaveSession
import com.kobietka.taskmanagement.repository.inter.TaskSessionRepository
import com.kobietka.taskmanagement.ui.util.DateUtil
import com.kobietka.taskmanagement.ui.util.Timer
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class TimeMeasureViewModel
@Inject constructor(
    private val saveSession: SaveSession,
    private val loadSessions: LoadSessions,
    private val loadTask: LoadTask) : ViewModel() {

    private val _timeText = MutableLiveData("00:00")
    private val _time = MutableLiveData(0)
    private val _taskName = MutableLiveData("")
    private val _taskSessions = MutableLiveData<List<TaskSessionEntity>>(listOf())
    private val _taskId = MutableLiveData(0)
    private val _projectId = MutableLiveData(0)
    private val _loadingFinished = MutableLiveData(false)
    private val timer = Timer().onTick { seconds ->
        _timeText.value = convertTime(seconds)
        _time.value = seconds
    }

    fun timeText(): LiveData<String> = _timeText
    fun taskName(): LiveData<String> = _taskName
    fun taskSessions(): LiveData<List<TaskSessionEntity>> = _taskSessions
    fun taskId(): LiveData<Int> = _taskId
    fun projectId(): LiveData<Int> = _projectId
    fun loadingFinished(): LiveData<Boolean> = _loadingFinished
    fun time(): LiveData<Int> = _time

    fun loadTaskData(taskId: Int){
        _loadingFinished.value = false
        loadTask.execute(
            taskId = taskId,
            onFinish = { loadedTask ->
                _taskName.value = loadedTask.name
                _taskId.value = loadedTask.id
                _projectId.value = loadedTask.projectId
                loadSessions.execute(
                    taskId = taskId,
                    onFinish = { loadedSessions ->
                        _taskSessions.value = loadedSessions
                        _loadingFinished.value = true
                    }
                )
            }
        )
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
        saveSession.execute(
            taskId = taskId,
            timeInSeconds = timeInSeconds
        )
    }

    fun loadSessions(taskId: Int, onFinish: (List<TaskSessionEntity>) -> Unit){
        loadSessions.execute(
            taskId = taskId,
            onFinish = onFinish
        )
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