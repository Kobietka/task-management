package com.kobietka.taskmanagement.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kobietka.taskmanagement.data.TaskEntity
import com.kobietka.taskmanagement.data.TaskStatusEntity
import com.kobietka.taskmanagement.repository.inter.TaskRepository
import com.kobietka.taskmanagement.repository.inter.TaskStatusRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.DateFormat
import java.util.*
import javax.inject.Inject


@HiltViewModel
class TasksViewModel
@Inject constructor(private val taskRepository: TaskRepository,
                    private val taskStatusRepository: TaskStatusRepository): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _taskDate = MutableLiveData("Due date")

    fun taskDate(): LiveData<String> {
        return _taskDate
    }

    fun setDate(date: String){
        _taskDate.value = date
    }

    fun clearDate(){
        _taskDate.value = "Due date"
    }

    fun insertTask(projectId: Int, name: String, description: String, statusId: Int, dueDate: String, onFinish: () -> Unit){
        val calendar = Calendar.getInstance()
        val currentDate = "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH)}/${calendar.get(Calendar.YEAR)}"

        compositeDisposable.add(
            taskRepository.insert(
                TaskEntity(
                    id = null,
                    projectId = projectId,
                    name = name,
                    description = description,
                    creationDate = currentDate,
                    dueDate = dueDate,
                    statusId = statusId
                )
            ).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(onFinish)
        )
    }

    fun insertTask(taskEntity: TaskEntity, onFinish: () -> Unit){
        compositeDisposable.add(
            taskRepository.insert(
                taskEntity = taskEntity
            ).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(onFinish)
        )
    }

    fun deleteTask(taskId: Int){
        taskRepository.deleteById(taskId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun loadTask(taskId: Int, onFinish: (TaskEntity) -> Unit){
        compositeDisposable.add(
            taskRepository.getById(taskId)
                .observeOn(AndroidSchedulers.mainThread())
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