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
    private val _projects = MutableLiveData<List<ProjectData>>(listOf())

    fun projects(): LiveData<List<ProjectData>> {
        return _projects
    }

    fun loadProjects(onFinish: (List<ProjectEntity>) -> Unit){
        compositeDisposable.add(
            projectRepository.getAllMaybe()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onFinish)
        )
    }

    @SuppressLint("CheckResult")
    fun loadData(){
        compositeDisposable.add(
            projectRepository.getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { projects ->
                    val projectsData = mutableListOf<ProjectData>()
                    projects.forEachIndexed { index, projectEntity ->
                        projectRepository.getNumberOfTasksInProject(projectEntity.id!!)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe { numberOfTasks ->
                                projectsData.add(ProjectData(projectEntity, numberOfTasks))
                                if(index == projects.size - 1){
                                    _projects.value = projectsData
                                }
                            }
                    }
                }
        )
    }

}