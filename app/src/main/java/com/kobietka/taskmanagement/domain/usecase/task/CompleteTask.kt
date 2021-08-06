package com.kobietka.taskmanagement.domain.usecase.task

import android.annotation.SuppressLint
import com.kobietka.taskmanagement.repository.inter.TaskRepository
import com.kobietka.taskmanagement.repository.inter.TaskStatusRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class CompleteTask
@Inject constructor(private val taskRepository: TaskRepository,
                    private val taskStatusRepository: TaskStatusRepository){

    @SuppressLint("CheckResult")
    fun execute(taskId: Int){
        taskStatusRepository.getAll()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe { statuses ->
                val status = statuses.first { status -> status.name == "Completed" }
                taskRepository.getById(taskId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe { taskEntity ->
                        taskRepository.insert(taskEntity.copy(statusId = status.id!!))
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe()
                    }
            }
    }

}