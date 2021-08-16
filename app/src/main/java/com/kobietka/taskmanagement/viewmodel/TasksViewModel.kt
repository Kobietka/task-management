package com.kobietka.taskmanagement.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kobietka.taskmanagement.data.entity.TaskEntity
import com.kobietka.taskmanagement.data.entity.TaskStatusEntity
import com.kobietka.taskmanagement.domain.usecase.task.*
import com.kobietka.taskmanagement.domain.usecase.taskstatus.LoadTaskStatuses
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class TasksViewModel
@Inject constructor(private val loadTaskStatuses: LoadTaskStatuses,
                    private val updateTask: UpdateTask,
                    private val archiveTask: ArchiveTask,
                    private val completeTask: CompleteTask,
                    private val deleteTask: DeleteTask,
                    private val loadTask: LoadTask,
                    private val insertTask: InsertTask,
                    private val unarchiveTask: UnarchiveTask): ViewModel() {

    private val _taskStatuses = MutableLiveData<List<TaskStatusEntity>>(listOf())

    fun taskStatuses(): LiveData<List<TaskStatusEntity>> = _taskStatuses

    fun loadTaskStatusesData(){
        loadTaskStatuses { statuses ->
            _taskStatuses.value = statuses
        }
    }

    fun updateTask(
        taskId: Int,
        name: String,
        description: String,
        newStatusId: Int,
        dueDate: String,
        onFinish: () -> Unit
    ){
       updateTask.execute(
           taskId = taskId,
           name = name,
           description = description,
           newStatusId = newStatusId,
           dueDate = dueDate,
           onFinish = onFinish
       )
    }

    fun changeTaskStatusToCompleted(
        taskId: Int,
        onFinish: () -> Unit
    ){
        completeTask.execute(
            taskId = taskId,
            onFinish = onFinish
        )
    }

    fun insertTask(
        projectId: Int,
        name: String,
        description: String,
        statusId: Int,
        dueDate: String,
        onFinish: () -> Unit){
        insertTask.execute(
            projectId = projectId,
            name = name,
            description = description,
            statusId = statusId,
            dueDate = dueDate,
            onFinish = onFinish
        )
    }

    fun unarchiveTask(taskId: Int, onFinish: () -> Unit){
        unarchiveTask.execute(
            taskId = taskId,
            onFinish = onFinish
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
        loadTask.execute(
            taskId = taskId,
            onFinish = onFinish
        )
    }

    fun loadTaskStatuses(onFinish: (List<TaskStatusEntity>) -> Unit){
        loadTaskStatuses.execute(onFinish = onFinish)
    }

}