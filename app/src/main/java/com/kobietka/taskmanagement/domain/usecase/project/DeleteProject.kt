package com.kobietka.taskmanagement.domain.usecase.project

import android.annotation.SuppressLint
import com.kobietka.taskmanagement.repository.inter.ProjectRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class DeleteProject
@Inject constructor(private val projectRepository: ProjectRepository){

    @SuppressLint("CheckResult")
    fun execute(projectId: Int, onFinish: () -> Unit){
        projectRepository.deleteById(projectId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(onFinish)
    }

}