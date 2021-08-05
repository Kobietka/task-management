package com.kobietka.taskmanagement.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kobietka.taskmanagement.data.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): Database {
        val database = Room.databaseBuilder(
            context,
            Database::class.java,
            "app_database"
        ).fallbackToDestructiveMigration().build()

        database.taskStatusDao().insert(TaskStatusEntity(1, "Not started"))
            .subscribeOn(Schedulers.io())
            .subscribe()
        database.taskStatusDao().insert(TaskStatusEntity(2, "In progress"))
            .subscribeOn(Schedulers.io())
            .subscribe()
        database.taskStatusDao().insert(TaskStatusEntity(3, "Completed"))
            .subscribeOn(Schedulers.io())
            .subscribe()

        return database
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

    @Provides
    fun provideTaskSessionDao(database: Database): TaskSessionDao {
        return database.taskSessionDao()
    }

}

















