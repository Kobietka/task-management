package com.kobietka.taskmanagement.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kobietka.taskmanagement.data.entity.TaskStatusEntity
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable


@Dao
interface TaskStatusDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(taskStatusEntity: TaskStatusEntity): Completable

    @Query("select * from taskStatus where id = :id")
    fun getById(id: Int): Maybe<TaskStatusEntity>

    @Query("delete from taskStatus where id = :id")
    fun deleteById(id: Int): Completable

    @Query("select * from taskStatus")
    fun getAll(): Maybe<List<TaskStatusEntity>>

    @Query("select * from taskStatus")
    fun getAllObservable(): Observable<List<TaskStatusEntity>>

}