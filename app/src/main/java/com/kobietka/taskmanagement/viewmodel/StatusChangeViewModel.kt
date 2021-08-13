package com.kobietka.taskmanagement.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kobietka.taskmanagement.data.entity.StatusEventEntity
import com.kobietka.taskmanagement.domain.usecase.statusevent.LoadStatusEventsForProject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class StatusChangeViewModel
@Inject constructor(private val loadStatusEventsForProject: LoadStatusEventsForProject): ViewModel() {

    private val _statusChanges = MutableLiveData<List<StatusEventEntity>>(listOf())

    fun statusChanges(): LiveData<List<StatusEventEntity>> = _statusChanges

    fun loadStatusEventsData(projectId: Int){
        loadStatusEventsForProject(
            projectId = projectId,
            onFinish = { events ->
                _statusChanges.value = events
            }
        )
    }

    fun loadStatusEventsForProject(projectId: Int, onFinish: (List<StatusEventEntity>) -> Unit){
        loadStatusEventsForProject.execute(
            projectId = projectId,
            onFinish = onFinish
        )
    }

}