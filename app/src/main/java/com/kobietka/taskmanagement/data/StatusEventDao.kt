package com.kobietka.taskmanagement.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Maybe


@Dao
interface StatusEventDao {

    @Insert
    fun insert(statusEventEntity: StatusEventEntity): Completable

    @Query("select * from statusEvent")
    fun getAll(): Maybe<List<StatusEventEntity>>

    @Query("select * from statusEvent where projectId = :projectId")
    fun getAllByProject(projectId: Int): Maybe<List<StatusEventEntity>>

}