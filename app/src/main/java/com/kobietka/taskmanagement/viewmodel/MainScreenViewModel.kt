package com.kobietka.taskmanagement.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kobietka.taskmanagement.data.entity.ProjectEntity
import com.kobietka.taskmanagement.domain.usecase.project.LoadProjects
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MainScreenViewModel
@Inject constructor(private val loadProjects: LoadProjects): ViewModel() {
    private val _projects = MutableLiveData<List<ProjectEntity>>(listOf())
    private val _isProjectLoadingFinished = MutableLiveData(false)

    fun projects(): LiveData<List<ProjectEntity>> = _projects
    fun isProjectLoadingFinished(): LiveData<Boolean> = _isProjectLoadingFinished

    fun loadProjects(){
        _isProjectLoadingFinished.value = false
        loadProjects.execute { loadedProjects ->
            _projects.value = loadedProjects
            _isProjectLoadingFinished.value = true
        }
    }

}