package com.kobietka.taskmanagement.repository.inter

import com.kobietka.taskmanagement.data.TaskStatusEntity
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable

interface TaskStatusRepository {
    fun insert(taskStatusEntity: TaskStatusEntity): Completable
    fun getById(id: Int): Maybe<TaskStatusEntity>
    fun deleteById(id: Int): Completable
    fun getAll(): Maybe<List<TaskStatusEntity>>
    fun getAllObservable(): Observable<List<TaskStatusEntity>>
}