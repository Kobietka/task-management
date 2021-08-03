package com.kobietka.taskmanagement.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "task",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("projectId"),
            onDelete = CASCADE,
            onUpdate = CASCADE
        )
    ]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo val projectId: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val description: String,
    @ColumnInfo val creationDate: String,
    @ColumnInfo val dueDate: String,
    @ColumnInfo val statusId: Int,
    @ColumnInfo val isArchived: Boolean,
    @ColumnInfo val completionTimeInSeconds: Int?
)
