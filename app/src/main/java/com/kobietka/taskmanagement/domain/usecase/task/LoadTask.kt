package com.kobietka.taskmanagement.domain.usecase.task

import android.annotation.SuppressLint
import com.kobietka.taskmanagement.data.entity.TaskEntity
import com.kobietka.taskmanagement.repository.inter.TaskRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class LoadTask
@Inject constructor(private val taskRepository: TaskRepository){

    @SuppressLint("CheckResult")
    fun execute(taskId: Int, onFinish: (TaskEntity) -> Unit){
        taskRepository.getById(taskId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(onFinish)
    }

}