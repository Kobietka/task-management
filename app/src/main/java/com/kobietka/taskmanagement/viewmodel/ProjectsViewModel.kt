package com.kobietka.taskmanagement.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kobietka.taskmanagement.data.entity.ProjectEntity
import com.kobietka.taskmanagement.data.entity.TaskEntity
import com.kobietka.taskmanagement.data.entity.TaskStatusEntity
import com.kobietka.taskmanagement.domain.usecase.project.*
import com.kobietka.taskmanagement.domain.usecase.taskstatus.LoadTaskStatus
import com.kobietka.taskmanagement.repository.inter.ProjectRepository
import com.kobietka.taskmanagement.repository.inter.TaskRepository
import com.kobietka.taskmanagement.ui.screen.ProjectDetailsScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import kotlin.system.measureTimeMillis


@HiltViewModel
class ProjectsViewModel
@Inject constructor(private val loadTaskStatus: LoadTaskStatus,
                    private val insertProject: InsertProject,
                    private val updateProject: UpdateProject,
                    private val deleteProject: DeleteProject,
                    private val loadProjectWithTasks: LoadProjectWithTasks,
                    private val loadProject: LoadProject,
                    private val loadProjects: LoadProjects): ViewModel() {

    private val _projectName = MutableLiveData("")
    private val _projectDescription = MutableLiveData("")
    private val _projectTasks = MutableLiveData<List<TaskEntity>>(listOf())
    private val _projectId = MutableLiveData(0)
    private val _loadingFinished = MutableLiveData(false)

    fun projectName(): LiveData<String> = _projectName
    fun projectDescription(): LiveData<String> = _projectDescription
    fun projectTasks(): LiveData<List<TaskEntity>> = _projectTasks
    fun projectId(): LiveData<Int> = _projectId
    fun loadingFinished(): LiveData<Boolean> = _loadingFinished

    fun loadProjectData(projectId: Int){
        _loadingFinished.value = false
        loadProjectWithTasks(
            projectId = projectId,
            onFinish = { projectEntity, tasks ->
                _projectId.value = projectEntity.id
                _projectName.value = projectEntity.name
                _projectDescription.value = projectEntity.description
                _projectTasks.value = tasks
                _loadingFinished.value = true
            }
        )
    }

    fun insertProject(name: String, description: String, onFinish: () -> Unit){
        insertProject.execute(
            name = name,
            description = description,
            onFinish = onFinish
        )
    }

    fun updateProject(
        oldProjectEntity: ProjectEntity,
        name: String,
        description: String,
        onFinish: () -> Unit
    ){
        updateProject.execute(
            oldProjectEntity = oldProjectEntity,
            name = name,
            description = description,
            onFinish = onFinish
        )
    }

    fun deleteProject(projectId: Int, onFinish: () -> Unit){
        deleteProject.execute(
            projectId = projectId,
            onFinish = onFinish
        )
    }

    fun loadProjectWithTasks(
        projectId: Int,
        onFinish: (ProjectEntity, List<TaskEntity>) -> Unit
    ){
        loadProjectWithTasks.execute(
            projectId = projectId,
            onFinish = onFinish
        )
    }

    fun loadProject(id: Int, onFinish: (ProjectEntity) -> Unit){
        loadProject.execute(
            id = id,
            onFinish = onFinish
        )
    }

    fun loadTaskStatus(statusId: Int, onFinish: (TaskStatusEntity) -> Unit){
        loadTaskStatus.execute(statusId = statusId, onFinish = onFinish)
    }

    fun loadProjects(onFinish: (List<ProjectEntity>) -> Unit){
        loadProjects.execute(onFinish = onFinish)
    }

}













