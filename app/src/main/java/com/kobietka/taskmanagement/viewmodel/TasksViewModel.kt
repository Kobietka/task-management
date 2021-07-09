package com.kobietka.taskmanagement.viewmodel


import androidx.lifecycle.ViewModel
import com.kobietka.taskmanagement.data.TaskEntity
import com.kobietka.taskmanagement.data.TaskStatusEntity
import com.kobietka.taskmanagement.repository.inter.TaskRepository
import com.kobietka.taskmanagement.repository.inter.TaskStatusRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject


@HiltViewModel
class TasksViewModel
@Inject constructor(private val taskRepository: TaskRepository,
                    private val taskStatusRepository: TaskStatusRepository): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    fun insertTask(projectId: Int, name: String, description: String, statusId: Int, onFinish: () -> Unit){
        val currentDate = "${Calendar.DAY_OF_MONTH}/${Calendar.MONTH}/${Calendar.YEAR} ${Calendar.HOUR}:${Calendar.MINUTE}:${Calendar.SECOND}"

        compositeDisposable.add(
            taskRepository.insert(
                TaskEntity(
                    id = null,
                    projectId = projectId,
                    name = name,
                    description = description,
                    creationDate = currentDate,
                    statusId = statusId
                )
            ).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(onFinish)
        )
    }

    fun loadTaskStatuses(onFinish: (List<TaskStatusEntity>) -> Unit){
        compositeDisposable.add(
            taskStatusRepository.getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(onFinish)
        )
    }

}