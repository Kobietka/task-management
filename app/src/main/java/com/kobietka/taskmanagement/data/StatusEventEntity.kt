package com.kobietka.taskmanagement.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "statusEvent",
    foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("taskId")
        ),
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("projectId")
        )
    ]
)
data class StatusEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo val taskId: Int,
    @ColumnInfo val fromStatus: Int,
    @ColumnInfo val toStatus: Int,
    @ColumnInfo val projectId: Int,
    @ColumnInfo val date: String
)
