package com.kobietka.taskmanagement.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "taskSession",
    foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("taskId")
        )
    ]
)
data class TaskSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo val taskId: Int,
    @ColumnInfo val projectId: Int,
    @ColumnInfo val timeInSeconds: Int,
    @ColumnInfo val date: String
)
