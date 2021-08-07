package com.kobietka.taskmanagement.domain.usecase.statusevent

import android.annotation.SuppressLint
import com.kobietka.taskmanagement.data.entity.StatusEventEntity
import com.kobietka.taskmanagement.repository.inter.StatusEventRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class LoadStatusEventsForProject
@Inject constructor(private val statusEventRepository: StatusEventRepository){

    @SuppressLint("CheckResult")
    fun execute(projectId: Int, onFinish: (List<StatusEventEntity>) -> Unit){
        statusEventRepository.getAllByProject(projectId = projectId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(onFinish)
    }

}