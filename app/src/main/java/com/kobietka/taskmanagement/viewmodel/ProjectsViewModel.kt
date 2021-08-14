package com.kobietka.taskmanagement.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kobietka.taskmanagement.data.entity.ProjectEntity
import com.kobietka.taskmanagement.data.entity.TaskEntity
import com.kobietka.taskmanagement.data.entity.TaskStatusEntity
import com.kobietka.taskmanagement.domain.usecase.project.*
import com.kobietka.taskmanagement.domain.usecase.taskstatus.LoadTaskStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ProjectsViewModel
@Inject constructor(private val insertProject: InsertProject,
                    private val updateProject: UpdateProject,
                    private val deleteProject: DeleteProject,
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
        projectId: Int,
        name: String,
        description: String,
        onFinish: () -> Unit
    ){
        updateProject.execute(
            projectId = projectId,
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

    fun loadProject(id: Int, onFinish: (ProjectEntity) -> Unit){
        loadProject.execute(
            id = id,
            onFinish = onFinish
        )
    }

    fun loadProjects(onFinish: (List<ProjectEntity>) -> Unit){
        loadProjects.execute(onFinish = onFinish)
    }

}













