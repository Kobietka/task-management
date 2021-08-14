package com.kobietka.taskmanagement.domain.usecase.tasksession

import android.annotation.SuppressLint
import com.kobietka.taskmanagement.data.entity.TaskSessionEntity
import com.kobietka.taskmanagement.repository.inter.TaskSessionRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class LoadSessionsForProject
@Inject constructor(private val sessionsRepository: TaskSessionRepository){

    @SuppressLint("CheckResult")
    fun execute(
        projectId: Int,
        onFinish: (List<TaskSessionEntity>) -> Unit
    ){
        sessionsRepository.getAllByProjectId(projectId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(onFinish)
    }
    
}