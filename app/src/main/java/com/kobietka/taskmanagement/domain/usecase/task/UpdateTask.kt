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
        oldTaskEntity: TaskEntity,
        name: String,
        description: String,
        newStatusId: Int,
        dueDate: String,
        onFinish: () -> Unit
    ){
        taskRepository.insert(
            taskEntity = oldTaskEntity.copy(
                name = name,
                description = description,
                statusId = newStatusId,
                dueDate = dueDate
            )
        ).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                statusEventRepository.insert(
                    statusEventEntity = StatusEventEntity(
                        id = null,
                        taskId = oldTaskEntity.id!!,
                        fromStatus = oldTaskEntity.statusId,
                        toStatus = newStatusId,
                        projectId = oldTaskEntity.projectId,
                        date = dateUtil.getCurrentDate()
                    )
                ).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(onFinish)
            }
    }

}