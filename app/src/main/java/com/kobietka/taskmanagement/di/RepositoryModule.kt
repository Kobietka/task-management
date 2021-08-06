package com.kobietka.taskmanagement.di

import com.kobietka.taskmanagement.data.*
import com.kobietka.taskmanagement.repository.impl.*
import com.kobietka.taskmanagement.repository.inter.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideProjectRepository(projectDao: ProjectDao): ProjectRepository {
        return ProjectRepositoryImpl(projectDao)
    }

    @Provides
    @Singleton
    fun provideTaskRepository(taskDao: TaskDao): TaskRepository {
        return TaskRepositoryImpl(taskDao)
    }

    @Provides
    @Singleton
    fun provideTaskStatusRepository(taskStatusDao: TaskStatusDao): TaskStatusRepository {
        return TaskStatusRepositoryImpl(taskStatusDao)
    }

    @Provides
    @Singleton
    fun provideTaskSessionRepository(taskSessionDao: TaskSessionDao): TaskSessionRepository {
        return TaskSessionRepositoryImpl(taskSessionDao)
    }

    @Provides
    @Singleton
    fun provideStatusEventRepository(statusEventDao: StatusEventDao): StatusEventRepository {
        return StatusEventRepositoryImpl(statusEventDao)
    }

}















