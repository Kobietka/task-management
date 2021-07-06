package com.kobietka.taskmanagement.di

import com.kobietka.taskmanagement.data.ProjectDao
import com.kobietka.taskmanagement.data.TaskDao
import com.kobietka.taskmanagement.data.TaskStatusDao
import com.kobietka.taskmanagement.repository.impl.ProjectRepositoryImpl
import com.kobietka.taskmanagement.repository.impl.TaskRepositoryImpl
import com.kobietka.taskmanagement.repository.impl.TaskStatusRepositoryImpl
import com.kobietka.taskmanagement.repository.inter.ProjectRepository
import com.kobietka.taskmanagement.repository.inter.TaskRepository
import com.kobietka.taskmanagement.repository.inter.TaskStatusRepository
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

}















