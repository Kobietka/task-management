package com.kobietka.taskmanagement.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single


@Dao
interface ProjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(projectEntity: ProjectEntity): Completable

    @Query("select * from project")
    fun getAll(): Observable<List<ProjectEntity>>

    @Query("select * from project")
    fun getAllMaybe(): Maybe<List<ProjectEntity>>

    @Query("select * from project where id = :id")
    fun getById(id: Int): Maybe<ProjectEntity>

    @Query("select count(*) from task where projectId = :id")
    fun getNumberOfTasksInProject(id: Int): Maybe<Int>
    
    @Query("delete from project where id = :id")
    fun deleteById(id: Int): Completable

}