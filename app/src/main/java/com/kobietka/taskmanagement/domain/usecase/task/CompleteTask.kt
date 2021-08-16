package com.kobietka.taskmanagement.domain.usecase.task

import android.annotation.SuppressLint
import com.kobietka.taskmanagement.data.entity.StatusEventEntity
import com.kobietka.taskmanagement.repository.inter.StatusEventRepository
import com.kobietka.taskmanagement.repository.inter.TaskRepository
import com.kobietka.taskmanagement.repository.inter.TaskStatusRepository
import com.kobietka.taskmanagement.ui.util.DateUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class CompleteTask
@Inject constructor(private val taskRepository: TaskRepository,
                    private val taskStatusRepository: TaskStatusRepository,
                    private val statusEventRepository: StatusEventRepository,
                    private val dateUtil: DateUtil){

    @SuppressLint("CheckResult")
    fun execute(taskId: Int, onFinish: () -> Unit){
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
                            .subscribe {
                                if(status.id != taskEntity.statusId){
                                    statusEventRepository.insert(
                                        statusEventEntity = StatusEventEntity(
                                            id = null,
                                            taskId = taskEntity.id!!,
                                            fromStatus = taskEntity.statusId,
                                            toStatus = status.id,
                                            projectId = taskEntity.projectId,
                                            date = dateUtil.getCurrentDate()
                                        )
                                    ).observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(onFinish)
                                }
                            }
                    }
            }
    }

}