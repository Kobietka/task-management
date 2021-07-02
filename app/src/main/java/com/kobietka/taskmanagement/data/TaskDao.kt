package com.kobietka.taskmanagement.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Observable


@Dao
interface TaskDao {

    @Insert
    fun insert(taskEntity: TaskEntity): Completable

    @Query("select * from task where projectId = :projectId")
    fun getAllByProjectId(projectId: Int): Observable<List<TaskEntity>>

    @Query("delete from task where id = :id")
    fun deleteById(id: Int): Completable

}