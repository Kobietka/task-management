package com.kobietka.taskmanagement.repository.inter

import com.kobietka.taskmanagement.data.TaskEntity
import io.reactivex.Completable
import io.reactivex.Observable


interface TaskRepository {
    fun insert(taskEntity: TaskEntity): Completable
    fun getAllByProjectId(projectId: Int): Observable<List<TaskEntity>>
    fun deleteById(id: Int): Completable
}