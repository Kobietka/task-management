package com.kobietka.taskmanagement.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kobietka.taskmanagement.data.ProjectEntity
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
    private val _projects = MutableLiveData<List<ProjectEntity>>(listOf())

    fun projects(): LiveData<List<ProjectEntity>> {
        return _projects
    }

    fun loadData(){
        compositeDisposable.add(
            projectRepository.getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    _projects.value = it
                }
        )
    }

}