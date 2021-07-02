package com.kobietka.taskmanagement.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Observable


@Dao
interface ProjectDao {

    @Insert
    fun insert(projectEntity: ProjectEntity): Completable

    @Query("select * from project")
    fun getAll(): Observable<List<ProjectEntity>>

    @Query("delete from project where id = :id")
    fun deleteById(id: Int): Completable

}