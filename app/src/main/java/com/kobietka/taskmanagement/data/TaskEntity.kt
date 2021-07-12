package com.kobietka.taskmanagement.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "task", foreignKeys = [ForeignKey(entity = ProjectEntity::class, parentColumns = arrayOf("id"), childColumns = arrayOf("projectId"))])
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo val projectId: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val description: String,
    @ColumnInfo val creationDate: String,
    @ColumnInfo val statusId: Int
)
