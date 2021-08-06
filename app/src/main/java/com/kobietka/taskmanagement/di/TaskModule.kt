package com.kobietka.taskmanagement.di

import com.kobietka.taskmanagement.domain.usecase.task.*
import com.kobietka.taskmanagement.repository.inter.StatusEventRepository
import com.kobietka.taskmanagement.repository.inter.TaskRepository
import com.kobietka.taskmanagement.repository.inter.TaskStatusRepository
import com.kobietka.taskmanagement.ui.util.DateUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
class TaskModule {

    @Provides
    fun provideUpdateTask(
        taskRepository: TaskRepository,
        statusEventRepository: StatusEventRepository,
        dateUtil: DateUtil): UpdateTask {
        return UpdateTask(
            taskRepository = taskRepository,
            statusEventRepository = statusEventRepository,
            dateUtil = dateUtil
        )
    }

    @Provides
    fun provideArchiveTask(taskRepository: TaskRepository): ArchiveTask {
        return ArchiveTask(taskRepository = taskRepository)
    }

    @Provides
    fun provideCompleteTask(
        taskRepository: TaskRepository,
        taskStatusRepository: TaskStatusRepository) : CompleteTask {
        return CompleteTask(
            taskRepository = taskRepository,
            taskStatusRepository = taskStatusRepository
        )
    }

    @Provides
    fun provideDeleteTask(taskRepository: TaskRepository) : DeleteTask {
        return DeleteTask(taskRepository = taskRepository)
    }

    @Provides
    fun provideLoadTask(taskRepository: TaskRepository): LoadTask {
        return LoadTask(taskRepository = taskRepository)
    }

    @Provides
    fun provideInsertTask(
        taskRepository: TaskRepository,
        dateUtil: DateUtil): InsertTask {
        return InsertTask(
            taskRepository = taskRepository,
            dateUtil = dateUtil
        )
    }

}