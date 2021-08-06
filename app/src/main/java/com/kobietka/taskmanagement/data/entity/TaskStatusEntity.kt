package com.kobietka.taskmanagement.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "taskStatus")
data class TaskStatusEntity(
    @PrimaryKey val id: Int?,
    @ColumnInfo val name: String
)
