package com.kobietka.taskmanagement.viewmodel

import androidx.lifecycle.ViewModel
import com.kobietka.taskmanagement.data.entity.StatusEventEntity
import com.kobietka.taskmanagement.domain.usecase.statusevent.LoadStatusEventsForProject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class StatusChangeViewModel
@Inject constructor(private val loadStatusEventsForProject: LoadStatusEventsForProject): ViewModel() {

    fun loadStatusEventsForProject(projectId: Int, onFinish: (List<StatusEventEntity>) -> Unit){
        loadStatusEventsForProject.execute(
            projectId = projectId,
            onFinish = onFinish
        )
    }

}