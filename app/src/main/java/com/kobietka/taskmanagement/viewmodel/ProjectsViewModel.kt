package com.kobietka.taskmanagement.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.kobietka.taskmanagement.data.entity.ProjectEntity
import com.kobietka.taskmanagement.data.entity.TaskEntity
import com.kobietka.taskmanagement.data.entity.TaskStatusEntity
import com.kobietka.taskmanagement.domain.usecase.project.*
import com.kobietka.taskmanagement.domain.usecase.taskstatus.LoadTaskStatus
import com.kobietka.taskmanagement.repository.inter.ProjectRepository
import com.kobietka.taskmanagement.repository.inter.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


@HiltViewModel
class ProjectsViewModel
@Inject constructor(private val loadTaskStatus: LoadTaskStatus,
                    private val insertProject: InsertProject,
                    private val updateProject: UpdateProject,
                    private val deleteProject: DeleteProject,
                    private val loadProjectWithTasks: LoadProjectWithTasks,
                    private val loadProject: LoadProject,
                    private val loadProjects: LoadProjects): ViewModel() {

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













