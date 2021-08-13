package com.kobietka.taskmanagement.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kobietka.taskmanagement.data.entity.TaskEntity
import com.kobietka.taskmanagement.domain.usecase.project.LoadProjectWithTasks
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ProjectDetailsViewModel
@Inject constructor(private val loadProjectWithTasks: LoadProjectWithTasks) : ViewModel() {
    private val _projectName = MutableLiveData("")
    private val _projectDescription = MutableLiveData("")
    private val _projectTasks = MutableLiveData<List<TaskEntity>>(listOf())
    private val _projectId = MutableLiveData(0)
    private val _projectDetailsLoadingFinished = MutableLiveData(false)

    fun projectName(): LiveData<String> = _projectName
    fun projectDescription(): LiveData<String> = _projectDescription
    fun projectTasks(): LiveData<List<TaskEntity>> = _projectTasks
    fun projectId(): LiveData<Int> = _projectId
    fun loadingFinished(): LiveData<Boolean> = _projectDetailsLoadingFinished

    fun loadProjectData(projectId: Int){
        _projectDetailsLoadingFinished.value = false
        loadProjectWithTasks.execute(
            projectId = projectId,
            onFinish = { projectEntity, tasks ->
                _projectId.value = projectEntity.id
                _projectName.value = projectEntity.name
                _projectDescription.value = projectEntity.description
                _projectTasks.value = tasks
                _projectDetailsLoadingFinished.value = true
            }
        )
    }

}