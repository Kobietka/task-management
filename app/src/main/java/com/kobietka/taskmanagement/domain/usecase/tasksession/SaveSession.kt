package com.kobietka.taskmanagement.domain.usecase.tasksession

import com.kobietka.taskmanagement.data.entity.TaskSessionEntity
import com.kobietka.taskmanagement.repository.inter.TaskSessionRepository
import com.kobietka.taskmanagement.ui.util.DateUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class SaveSession
@Inject constructor(private val taskSessionRepository: TaskSessionRepository,
                    private val dateUtil: DateUtil){

    fun execute(
        taskId: Int,
        projectId: Int,
        timeInSeconds: Int
    ){
        taskSessionRepository.insert(
            TaskSessionEntity(
                id = null,
                timeInSeconds = timeInSeconds,
                date = dateUtil.getCurrentDate(),
                taskId = taskId,
                projectId = projectId
            )
        ).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

}