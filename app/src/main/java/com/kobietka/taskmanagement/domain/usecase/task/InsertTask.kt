package com.kobietka.taskmanagement.domain.usecase.task

import android.annotation.SuppressLint
import com.kobietka.taskmanagement.data.entity.TaskEntity
import com.kobietka.taskmanagement.repository.inter.TaskRepository
import com.kobietka.taskmanagement.ui.util.DateUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class InsertTask
@Inject constructor(private val taskRepository: TaskRepository,
                    private val dateUtil: DateUtil){

    @SuppressLint("CheckResult")
    fun execute(
        projectId: Int,
        name: String,
        description: String,
        statusId: Int,
        dueDate: String,
        onFinish: () -> Unit){
        taskRepository.insert(
            TaskEntity(
                id = null,
                projectId = projectId,
                name = name,
                description = description,
                creationDate = dateUtil.getCurrentDate(),
                dueDate = dueDate,
                statusId = statusId,
                isArchived = false
            )
        ).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(onFinish)
    }

}