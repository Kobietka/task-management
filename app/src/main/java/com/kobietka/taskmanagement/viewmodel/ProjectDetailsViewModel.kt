package com.kobietka.taskmanagement.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kobietka.taskmanagement.data.entity.TaskEntity
import com.kobietka.taskmanagement.data.entity.TaskSessionEntity
import com.kobietka.taskmanagement.domain.usecase.project.LoadProjectWithTasks
import com.kobietka.taskmanagement.domain.usecase.tasksession.LoadSessionsForProject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ProjectDetailsViewModel
@Inject constructor(
    private val loadProjectWithTasks: LoadProjectWithTasks,
    private val loadSessionsForProject: LoadSessionsForProject
) : ViewModel() {
    private val _projectName = MutableLiveData("")
    private val _projectDescription = MutableLiveData("")
    private val _projectTasks = MutableLiveData<List<TaskEntity>>(listOf())
    private val _projectFilteredTasks = MutableLiveData<List<TaskEntity>>(listOf())
    private val _projectId = MutableLiveData(0)
    private val _projectDetailsLoadingFinished = MutableLiveData(false)
    private val _taskSessions = MutableLiveData<List<TaskSessionEntity>>(listOf())

    fun projectName(): LiveData<String> = _projectName
    fun projectDescription(): LiveData<String> = _projectDescription
    fun projectFilteredTasks(): LiveData<List<TaskEntity>> = _projectFilteredTasks
    fun projectId(): LiveData<Int> = _projectId
    fun loadingFinished(): LiveData<Boolean> = _projectDetailsLoadingFinished
    fun taskSessions(): LiveData<List<TaskSessionEntity>> = _taskSessions

    fun loadProjectData(projectId: Int){
        _projectDetailsLoadingFinished.value = false
        loadProjectWithTasks.execute(
            projectId = projectId,
            onFinish = { projectEntity, tasks ->
                _projectId.value = projectEntity.id
                _projectName.value = projectEntity.name
                _projectDescription.value = projectEntity.description
                _projectTasks.value = tasks
                _projectFilteredTasks.value = tasks
                loadSessionsForProject.execute(
                    projectId = projectId,
                    onFinish = { loadedSessions ->
                        _taskSessions.value = loadedSessions
                        _projectDetailsLoadingFinished.value = true
                    }
                )
            }
        )
    }

    fun filterTasks(text: String){
        if(text.isEmpty()) _projectFilteredTasks.value = _projectTasks.value
        else {
            val filteredTasks = mutableListOf<TaskEntity>()
            _projectTasks.value?.forEach { task ->
                if(task.name.contains(text)) filteredTasks.add(task)
            }
            _projectFilteredTasks.value = filteredTasks
        }
    }

}