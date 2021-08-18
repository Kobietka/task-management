package com.kobietka.taskmanagement.domain.usecase.project

import android.annotation.SuppressLint
import com.kobietka.taskmanagement.repository.inter.ProjectRepository
import com.kobietka.taskmanagement.repository.inter.StatusEventRepository
import com.kobietka.taskmanagement.repository.inter.TaskRepository
import com.kobietka.taskmanagement.repository.inter.TaskSessionRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class DeleteProject
@Inject constructor(
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository,
    private val taskSessionRepository: TaskSessionRepository,
    private val statusEventRepository: StatusEventRepository
){

    @SuppressLint("CheckResult")
    fun execute(projectId: Int, onFinish: () -> Unit){
        taskSessionRepository.deleteAllTaskSessionsByProjectId(projectId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                taskRepository.deleteAllTasksFromProject(projectId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe {
                        statusEventRepository.deleteEventsByProjectId(projectId)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe {
                                projectRepository.deleteById(projectId)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(onFinish)
                            }
                    }
            }
    }

}