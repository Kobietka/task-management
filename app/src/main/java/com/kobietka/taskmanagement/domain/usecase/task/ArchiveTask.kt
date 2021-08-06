package com.kobietka.taskmanagement.domain.usecase.task

import android.annotation.SuppressLint
import com.kobietka.taskmanagement.repository.inter.TaskRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class ArchiveTask
@Inject constructor(private val taskRepository: TaskRepository){

    @SuppressLint("CheckResult")
    fun execute(taskId: Int, onFinish: () -> Unit){
        taskRepository.changeArchivedStatus(taskId, true)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(onFinish)
    }

}