package com.kobietka.taskmanagement.viewmodel


import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kobietka.taskmanagement.data.entity.TaskEntity
import com.kobietka.taskmanagement.data.entity.TaskStatusEntity
import com.kobietka.taskmanagement.domain.usecase.task.ArchiveTask
import com.kobietka.taskmanagement.domain.usecase.task.CompleteTask
import com.kobietka.taskmanagement.domain.usecase.task.DeleteTask
import com.kobietka.taskmanagement.domain.usecase.task.UpdateTask
import com.kobietka.taskmanagement.repository.inter.TaskRepository
import com.kobietka.taskmanagement.repository.inter.TaskStatusRepository
import com.kobietka.taskmanagement.ui.util.DateUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


@HiltViewModel
class TasksViewModel
@Inject constructor(private val taskRepository: TaskRepository,
                    private val taskStatusRepository: TaskStatusRepository,
                    private val dateUtil: DateUtil,
                    private val updateTask: UpdateTask,
                    private val archiveTask: ArchiveTask,
                    private val completeTask: CompleteTask,
                    private val deleteTask: DeleteTask): ViewModel() {

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

    fun updateTask(
        oldTaskEntity: TaskEntity,
        name: String,
        description: String,
        newStatusId: Int,
        dueDate: String,
        onFinish: () -> Unit
    ){
       updateTask.execute(
           oldTaskEntity = oldTaskEntity,
           name = name,
           description = description,
           newStatusId = newStatusId,
           dueDate = dueDate,
           onFinish = onFinish
       )
    }

    fun changeTaskStatusToCompleted(taskId: Int){
        completeTask.execute(taskId = taskId)
    }

    fun insertTask(projectId: Int, name: String, description: String, statusId: Int, dueDate: String, onFinish: () -> Unit){
        compositeDisposable.add(
            taskRepository.insert(
                TaskEntity(
                    id = null,
                    projectId = projectId,
                    name = name,
                    description = description,
                    creationDate = dateUtil.getCurrentDate(),
                    dueDate = dueDate,
                    statusId = statusId,
                    isArchived = false
                )
            ).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(onFinish)
        )
    }

    fun archiveTask(taskId: Int, onFinish: () -> Unit){
        archiveTask.execute(
            taskId = taskId,
            onFinish = onFinish
        )
    }

    fun deleteTask(taskId: Int, onFinish: () -> Unit){
        deleteTask.execute(
            taskId = taskId,
            onFinish = onFinish
        )
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