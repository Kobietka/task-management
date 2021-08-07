package com.kobietka.taskmanagement.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kobietka.taskmanagement.data.entity.ProjectEntity
import com.kobietka.taskmanagement.domain.model.ProjectData
import com.kobietka.taskmanagement.repository.inter.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


@HiltViewModel
class MainViewModel
@Inject constructor(private val projectRepository: ProjectRepository): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    fun loadProjects(onFinish: (List<ProjectEntity>) -> Unit){
        compositeDisposable.add(
            projectRepository.getAllMaybe()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onFinish)
        )
    }

}