package com.kobietka.taskmanagement.domain.usecase.project

import android.annotation.SuppressLint
import com.kobietka.taskmanagement.data.entity.ProjectEntity
import com.kobietka.taskmanagement.data.entity.TaskEntity
import com.kobietka.taskmanagement.repository.inter.ProjectRepository
import com.kobietka.taskmanagement.repository.inter.TaskRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class LoadProjectWithTasks
@Inject constructor(private val projectRepository: ProjectRepository,
                    private val taskRepository: TaskRepository){

    @SuppressLint("CheckResult")
    fun execute(projectId: Int, onFinish: (ProjectEntity, List<TaskEntity>) -> Unit){
        projectRepository.getById(projectId)
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
    }

}