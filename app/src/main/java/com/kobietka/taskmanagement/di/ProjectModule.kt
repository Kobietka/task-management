package com.kobietka.taskmanagement.di

import com.kobietka.taskmanagement.domain.usecase.project.*
import com.kobietka.taskmanagement.repository.inter.ProjectRepository
import com.kobietka.taskmanagement.repository.inter.StatusEventRepository
import com.kobietka.taskmanagement.repository.inter.TaskRepository
import com.kobietka.taskmanagement.repository.inter.TaskSessionRepository
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
    fun provideDeleteProject(
        projectRepository: ProjectRepository,
        taskRepository: TaskRepository,
        taskSessionRepository: TaskSessionRepository,
        statusEventRepository: StatusEventRepository
    ): DeleteProject {
        return DeleteProject(
            projectRepository = projectRepository,
            taskRepository = taskRepository,
            taskSessionRepository = taskSessionRepository,
            statusEventRepository = statusEventRepository
        )
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

    @Provides
    fun provideLoadProject(projectRepository: ProjectRepository): LoadProject {
        return LoadProject(projectRepository = projectRepository)
    }

    @Provides
    fun provideLoadProjects(projectRepository: ProjectRepository): LoadProjects {
        return LoadProjects(projectRepository = projectRepository)
    }

}