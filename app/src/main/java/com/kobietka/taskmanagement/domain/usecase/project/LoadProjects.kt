package com.kobietka.taskmanagement.domain.usecase.project

import android.annotation.SuppressLint
import com.kobietka.taskmanagement.data.entity.ProjectEntity
import com.kobietka.taskmanagement.repository.inter.ProjectRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class LoadProjects
@Inject constructor(private val projectRepository: ProjectRepository){

    @SuppressLint("CheckResult")
    fun execute(onFinish: (List<ProjectEntity>) -> Unit){
        projectRepository.getAllMaybe()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onFinish)
    }

}