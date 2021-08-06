package com.kobietka.taskmanagement.di

import com.kobietka.taskmanagement.ui.util.DateUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.*


@Module
@InstallIn(SingletonComponent::class)
class UtilModule {

    @Provides
    fun provideCalendar(): Calendar {
        return Calendar.getInstance()
    }

    @Provides
    fun provideDateUtil(calendar: Calendar): DateUtil {
        return DateUtil(calendar)
    }

}