package com.kobietka.taskmanagement.di

import com.kobietka.taskmanagement.domain.usecase.project.DeleteProject
import com.kobietka.taskmanagement.domain.usecase.project.InsertProject
import com.kobietka.taskmanagement.domain.usecase.project.LoadProjectWithTasks
import com.kobietka.taskmanagement.domain.usecase.project.UpdateProject
import com.kobietka.taskmanagement.repository.inter.ProjectRepository
import com.kobietka.taskmanagement.repository.inter.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
class ProjectModule {

    @Provides
    fun provideInsertProject(projectRepository: ProjectRepository): InsertProject {
        return InsertProject(projectRepository = projectRepository)
    }

    @Provides
    fun provideUpdateProject(projectRepository: ProjectRepository): UpdateProject {
        return UpdateProject(projectRepository = projectRepository)
    }

    @Provides
    fun provideDeleteProject(projectRepository: ProjectRepository): DeleteProject {
        return DeleteProject(projectRepository = projectRepository)
    }

    @Provides
    fun provideLoadProjectWithTasks(
        projectRepository: ProjectRepository,
        taskRepository: TaskRepository
    ): LoadProjectWithTasks {
        return LoadProjectWithTasks(
            projectRepository = projectRepository,
            taskRepository = taskRepository
        )
    }

}