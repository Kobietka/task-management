package com.kobietka.taskmanagement.domain.usecase.project

import android.annotation.SuppressLint
import com.kobietka.taskmanagement.data.entity.ProjectEntity
import com.kobietka.taskmanagement.repository.inter.ProjectRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class LoadProject
@Inject constructor(private val projectRepository: ProjectRepository){

    @SuppressLint("CheckResult")
    fun execute(id: Int, onFinish: (ProjectEntity) -> Unit){
        projectRepository.getById(id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(onFinish)
    }

}