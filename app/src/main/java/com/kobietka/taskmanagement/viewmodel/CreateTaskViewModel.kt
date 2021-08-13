package com.kobietka.taskmanagement.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kobietka.taskmanagement.data.entity.TaskStatusEntity
import com.kobietka.taskmanagement.domain.usecase.taskstatus.LoadTaskStatuses
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class CreateTaskViewModel
@Inject constructor(private val loadTaskStatuses: LoadTaskStatuses): ViewModel() {
    private val _taskStatuses = MutableLiveData<List<TaskStatusEntity>>(listOf())
    private val _projectId = MutableLiveData(0)
    private val _taskDate = MutableLiveData("Due date")

    fun taskStatuses(): LiveData<List<TaskStatusEntity>> = _taskStatuses
    fun projectId(): LiveData<Int> = _projectId
    fun taskDate(): LiveData<String> = _taskDate

    fun loadStatuses(projectId: Int){
        _projectId.value = projectId
        loadTaskStatuses.execute { loadedStatuses ->
            _taskStatuses.value = loadedStatuses
        }
    }

    fun setDate(date: String){
        _taskDate.value = date
    }

    fun clearDate(){
        _taskDate.value = "Due date"
    }

}