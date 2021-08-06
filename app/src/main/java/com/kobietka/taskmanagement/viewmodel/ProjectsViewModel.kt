package com.kobietka.taskmanagement.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.kobietka.taskmanagement.data.entity.ProjectEntity
import com.kobietka.taskmanagement.data.entity.TaskEntity
import com.kobietka.taskmanagement.data.entity.TaskStatusEntity
import com.kobietka.taskmanagement.domain.usecase.project.InsertProject
import com.kobietka.taskmanagement.domain.usecase.project.UpdateProject
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
@Inject constructor(private val projectRepository: ProjectRepository,
                    private val taskRepository: TaskRepository,
                    private val loadTaskStatus: LoadTaskStatus,
                    private val insertProject: InsertProject,
                    private val updateProject: UpdateProject): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

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
        compositeDisposable.add(
            projectRepository.deleteById(projectId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(onFinish)
        )
    }

    @SuppressLint("CheckResult")
    fun loadProjectWithTasks(id: Int, onFinish: (ProjectEntity, List<TaskEntity>) -> Unit){
        compositeDisposable.add(
            projectRepository.getById(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { project ->
                    taskRepository.getAllByProjectId(project.id!!)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe { tasks ->
                            onFinish(project, tasks)
                        }
                }
        )
    }

    fun loadProject(id: Int, onFinish: (ProjectEntity) -> Unit){
        compositeDisposable.add(
            projectRepository.getById(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(onFinish)
        )
    }

    fun loadTaskStatus(statusId: Int, onFinish: (TaskStatusEntity) -> Unit){
        loadTaskStatus.execute(statusId = statusId, onFinish = onFinish)
    }

}













