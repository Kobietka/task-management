package com.kobietka.taskmanagement.di

import com.kobietka.taskmanagement.domain.usecase.tasksession.LoadSessions
import com.kobietka.taskmanagement.domain.usecase.tasksession.SaveSession
import com.kobietka.taskmanagement.repository.inter.TaskSessionRepository
import com.kobietka.taskmanagement.ui.util.DateUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
class TaskSessionModule {

    @Provides
    fun provideSaveSession(
        taskSessionRepository: TaskSessionRepository,
        dateUtil: DateUtil
    ): SaveSession {
        return SaveSession(
            taskSessionRepository = taskSessionRepository,
            dateUtil = dateUtil
        )
    }

    @Provides
    fun provideLoadSessions(taskSessionRepository: TaskSessionRepository): LoadSessions {
        return LoadSessions(taskSessionRepository = taskSessionRepository)
    }

}