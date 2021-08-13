package com.kobietka.taskmanagement.domain.usecase.task

import android.annotation.SuppressLint
import com.kobietka.taskmanagement.data.entity.StatusEventEntity
import com.kobietka.taskmanagement.data.entity.TaskEntity
import com.kobietka.taskmanagement.repository.inter.StatusEventRepository
import com.kobietka.taskmanagement.repository.inter.TaskRepository
import com.kobietka.taskmanagement.ui.util.DateUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class UpdateTask
@Inject constructor(private val taskRepository: TaskRepository,
                    private val statusEventRepository: StatusEventRepository,
                    private val dateUtil: DateUtil){

    @SuppressLint("CheckResult")
    fun execute(
        taskId: Int,
        name: String,
        description: String,
        newStatusId: Int,
        dueDate: String,
        onFinish: () -> Unit
    ){
        taskRepository.getById(id = taskId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe { loadedTask ->
                taskRepository.insert(
                    taskEntity = loadedTask.copy(
                        name = name,
                        description = description,
                        statusId = newStatusId,
                        dueDate = dueDate
                    )
                ).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe {
                        if(newStatusId != loadedTask.statusId){
                            statusEventRepository.insert(
                                statusEventEntity = StatusEventEntity(
                                    id = null,
                                    taskId = loadedTask.id!!,
                                    fromStatus = loadedTask.statusId,
                                    toStatus = newStatusId,
                                    projectId = loadedTask.projectId,
                                    date = dateUtil.getCurrentDate()
                                )
                            ).observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(onFinish)
                        } else onFinish()
                    }
            }
    }

}