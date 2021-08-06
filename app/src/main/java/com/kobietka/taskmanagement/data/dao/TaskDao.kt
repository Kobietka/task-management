package com.kobietka.taskmanagement.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kobietka.taskmanagement.data.entity.TaskEntity
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable


@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(taskEntity: TaskEntity): Completable

    @Query("select * from task where projectId = :projectId")
    fun getAllByProjectId(projectId: Int): Observable<List<TaskEntity>>

    @Query("delete from task where id = :id")
    fun deleteById(id: Int): Completable

    @Query("delete from task where projectId = :projectId and id = :taskId")
    fun deleteFromProjectByProjectId(projectId: Int, taskId: Int): Completable

    @Query("select * from task where id = :id")
    fun getById(id: Int): Maybe<TaskEntity>

    @Query("update task set isArchived = :archived where id = :taskId")
    fun changeArchivedStatus(taskId: Int, archived: Boolean): Completable

}