package com.kobietka.taskmanagement.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.kobietka.taskmanagement.data.ProjectEntity
import com.kobietka.taskmanagement.data.TaskEntity
import com.kobietka.taskmanagement.data.TaskStatusEntity
import com.kobietka.taskmanagement.repository.inter.ProjectRepository
import com.kobietka.taskmanagement.repository.inter.TaskRepository
import com.kobietka.taskmanagement.repository.inter.TaskStatusRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


@HiltViewModel
class ProjectsViewModel
@Inject constructor(private val projectRepository: ProjectRepository,
                    private val taskRepository: TaskRepository,
                    private val taskStatusRepository: TaskStatusRepository): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    fun insertProject(name: String, description: String, onFinish: () -> Unit){
        compositeDisposable.add(
            projectRepository.insert(ProjectEntity(null, name, description))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onFinish)
        )
    }

    fun insertProject(projectEntity: ProjectEntity, onFinish: () -> Unit){
        compositeDisposable.add(
            projectRepository.insert(projectEntity)
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
        compositeDisposable.add(
            taskStatusRepository.getById(statusId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { taskStatus ->
                    onFinish(taskStatus)
                }
        )
    }

}













