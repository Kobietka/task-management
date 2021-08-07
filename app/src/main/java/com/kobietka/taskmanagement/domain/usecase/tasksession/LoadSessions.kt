package com.kobietka.taskmanagement.domain.usecase.tasksession

import android.annotation.SuppressLint
import com.kobietka.taskmanagement.data.entity.TaskSessionEntity
import com.kobietka.taskmanagement.repository.inter.TaskSessionRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class LoadSessions
@Inject constructor(private val taskSessionRepository: TaskSessionRepository){

    @SuppressLint("CheckResult")
    fun execute(
        taskId: Int,
        onFinish: (List<TaskSessionEntity>) -> Unit
    ) {
        taskSessionRepository.getAllByTaskId(taskId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(onFinish)
    }

}