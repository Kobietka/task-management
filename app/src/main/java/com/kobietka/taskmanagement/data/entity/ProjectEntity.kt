package com.kobietka.taskmanagement.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "project")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo val name: String,
    @ColumnInfo val description: String
)
