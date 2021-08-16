package com.kobietka.taskmanagement.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kobietka.taskmanagement.data.entity.TaskStatusEntity
import com.kobietka.taskmanagement.domain.usecase.task.LoadTask
import com.kobietka.taskmanagement.domain.usecase.taskstatus.LoadTaskStatuses
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class TaskDetailsViewModel
@Inject constructor(
    private val loadTaskStatuses: LoadTaskStatuses,
    private val loadTask: LoadTask
): ViewModel() {
    private val _taskStatuses = MutableLiveData<List<TaskStatusEntity>>(listOf())
    private val _taskId = MutableLiveData(0)
    private val _taskDate = MutableLiveData("Due date")
    private val _taskName = MutableLiveData("")
    private val _taskDescription = MutableLiveData("")
    private val _taskStatusId = MutableLiveData(0)
    private val _taskDueDate = MutableLiveData("Due date")
    private val _loadingFinished = MutableLiveData(false)
    private val _projectId = MutableLiveData(0)
    private val _isTaskArchived = MutableLiveData(false)

    fun taskStatuses(): LiveData<List<TaskStatusEntity>> = _taskStatuses
    fun taskId(): LiveData<Int> = _taskId
    fun taskDate(): LiveData<String> = _taskDate
    fun taskName(): MutableLiveData<String> = _taskName
    fun taskDescription(): MutableLiveData<String> = _taskDescription
    fun taskStatusId(): LiveData<Int> = _taskStatusId
    fun taskDueDate(): LiveData<String> = _taskDueDate
    fun loadingFinished(): LiveData<Boolean> = _loadingFinished
    fun projectId(): LiveData<Int> = _projectId
    fun isTaskArchived(): LiveData<Boolean> = _isTaskArchived

    fun loadTaskData(taskId: Int){
        _taskId.value = taskId
        _loadingFinished.value = false
        loadTask.execute(
            taskId = taskId,
            onFinish = { loadedTask ->
                _taskName.value = loadedTask.name
                _taskDescription.value = loadedTask.description
                _taskStatusId.value = loadedTask.statusId
                _taskDueDate.value = loadedTask.dueDate
                _projectId.value = loadedTask.projectId
                _isTaskArchived.value = loadedTask.isArchived
                loadTaskStatuses.execute { loadedStatuses ->
                    _taskStatuses.value = loadedStatuses
                    _loadingFinished.value = true
                }
            }
        )
    }

    fun setDate(date: String){
        _taskDate.value = date
    }

    fun clearDate(){
        _taskDate.value = "Due date"
    }

}