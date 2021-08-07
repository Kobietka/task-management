package com.kobietka.taskmanagement.di

import com.kobietka.taskmanagement.domain.usecase.statusevent.LoadStatusEventsForProject
import com.kobietka.taskmanagement.repository.inter.StatusEventRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
class StatusEventModule {

    @Provides
    fun provideLoadStatusEventsForProject(
        statusEventRepository: StatusEventRepository
    ): LoadStatusEventsForProject {
        return LoadStatusEventsForProject(statusEventRepository = statusEventRepository)
    }

}