package com.kobietka.taskmanagement.di

import com.kobietka.taskmanagement.domain.usecase.task.UpdateTask
import com.kobietka.taskmanagement.repository.inter.StatusEventRepository
import com.kobietka.taskmanagement.repository.inter.TaskRepository
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

}