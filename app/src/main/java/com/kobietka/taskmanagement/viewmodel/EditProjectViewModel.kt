package com.kobietka.taskmanagement.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kobietka.taskmanagement.domain.usecase.project.LoadProject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class EditProjectViewModel
@Inject constructor(private val loadProject: LoadProject): ViewModel() {
    private val _projectName = MutableLiveData("")
    private val _projectDescription = MutableLiveData("")
    private val _projectId = MutableLiveData(0)
    private val _loadingFinished = MutableLiveData(false)

    fun projectName(): LiveData<String> = _projectName
    fun projectDescription(): LiveData<String> = _projectDescription
    fun projectId(): LiveData<Int> = _projectId
    fun loadingFinished(): LiveData<Boolean> = _loadingFinished

    fun loadProjectData(projectId: Int){
        _loadingFinished.value = false
        loadProject.execute(
            id = projectId,
            onFinish = { loadedProject ->
                _projectName.value = loadedProject.name
                _projectDescription.value = loadedProject.description
                _projectId.value = loadedProject.id!!
                _loadingFinished.value = true
            }
        )
    }

}