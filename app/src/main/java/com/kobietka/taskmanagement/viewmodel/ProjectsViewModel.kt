package com.kobietka.taskmanagement.viewmodel

import androidx.lifecycle.ViewModel
import com.kobietka.taskmanagement.data.ProjectEntity
import com.kobietka.taskmanagement.repository.inter.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


@HiltViewModel
class ProjectsViewModel
@Inject constructor(private val projectRepository: ProjectRepository): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    fun insertProject(name: String, description: String, onFinish: () -> Unit){
        compositeDisposable.add(
            projectRepository.insert(ProjectEntity(null, name, description))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onFinish)
        )
    }

}