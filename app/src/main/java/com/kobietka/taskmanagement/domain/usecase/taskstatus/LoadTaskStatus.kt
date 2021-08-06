package com.kobietka.taskmanagement.domain.usecase.taskstatus

import android.annotation.SuppressLint
import com.kobietka.taskmanagement.data.entity.TaskStatusEntity
import com.kobietka.taskmanagement.repository.inter.TaskStatusRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class LoadTaskStatus
@Inject constructor(private val taskStatusRepository: TaskStatusRepository){

    @SuppressLint("CheckResult")
    fun execute(statusId: Int, onFinish: (TaskStatusEntity) -> Unit){
        taskStatusRepository.getById(statusId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe { taskStatus ->
                onFinish(taskStatus)
            }
    }

}