package com.kobietka.taskmanagement.domain.usecase.project

import android.annotation.SuppressLint
import com.kobietka.taskmanagement.data.entity.ProjectEntity
import com.kobietka.taskmanagement.repository.inter.ProjectRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class UpdateProject
@Inject constructor(private val projectRepository: ProjectRepository){

    @SuppressLint("CheckResult")
    fun execute(
        oldProjectEntity: ProjectEntity,
        name: String,
        description: String,
        onFinish: () -> Unit
    ){
        projectRepository.insert(
            projectEntity = oldProjectEntity.copy(
                name = name,
                description = description
            )
        ).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(onFinish)
    }

}