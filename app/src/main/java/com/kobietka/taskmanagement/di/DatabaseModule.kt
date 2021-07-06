package com.kobietka.taskmanagement.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kobietka.taskmanagement.data.Database
import com.kobietka.taskmanagement.data.ProjectDao
import com.kobietka.taskmanagement.data.TaskDao
import com.kobietka.taskmanagement.data.TaskStatusDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): Database {
        return Room.databaseBuilder(
            context,
            Database::class.java,
            "app_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideTaskDao(database: Database): TaskDao {
        return database.taskDao()
    }

    @Provides
    fun provideProjectDao(database: Database): ProjectDao {
        return database.projectDao()
    }

    @Provides
    fun provideTaskStatusDao(database: Database): TaskStatusDao {
        return database.taskStatusDao()
    }

}

















