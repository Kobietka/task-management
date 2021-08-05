package com.kobietka.taskmanagement.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ProjectEntity::class, TaskEntity::class, TaskStatusEntity::class, TaskSessionEntity::class], version = 17)
abstract class Database : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun taskDao(): TaskDao
    abstract fun taskStatusDao(): TaskStatusDao
    abstract fun taskSessionDao(): TaskSessionDao
}