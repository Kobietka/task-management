package com.kobietka.taskmanagement.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Maybe


@Dao
interface TaskSessionDao {

    @Insert
    fun insert(taskSessionEntity: TaskSessionEntity): Completable

    @Query("select * from taskSession where taskId = :taskId")
    fun getAllByTaskId(taskId: Int): Maybe<List<TaskSessionEntity>>

    @Query("delete from taskSession where id = :id")
    fun deleteById(id: Int): Completable

}