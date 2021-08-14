package com.kobietka.taskmanagement.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kobietka.taskmanagement.data.entity.TaskSessionEntity
import io.reactivex.Completable
import io.reactivex.Maybe


@Dao
interface TaskSessionDao {

    @Insert
    fun insert(taskSessionEntity: TaskSessionEntity): Completable

    @Query("select * from taskSession where taskId = :taskId")
    fun getAllByTaskId(taskId: Int): Maybe<List<TaskSessionEntity>>

    @Query("select * from taskSession where projectId = :projectId")
    fun getAllByProjectId(projectId: Int): Maybe<List<TaskSessionEntity>>

    @Query("delete from taskSession where id = :id")
    fun deleteById(id: Int): Completable

}