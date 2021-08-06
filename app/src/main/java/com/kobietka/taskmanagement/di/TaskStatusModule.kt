package com.kobietka.taskmanagement.di

import com.kobietka.taskmanagement.domain.usecase.taskstatus.LoadTaskStatus
import com.kobietka.taskmanagement.domain.usecase.taskstatus.LoadTaskStatuses
import com.kobietka.taskmanagement.repository.inter.TaskStatusRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
class TaskStatusModule {

    @Provides
    fun provideLoadTaskStatuses(taskStatusRepository: TaskStatusRepository): LoadTaskStatuses {
        return LoadTaskStatuses(taskStatusRepository = taskStatusRepository)
    }

    @Provides
    fun provideLoadTaskStatus(taskStatusRepository: TaskStatusRepository): LoadTaskStatus {
        return LoadTaskStatus(taskStatusRepository = taskStatusRepository)
    }

}