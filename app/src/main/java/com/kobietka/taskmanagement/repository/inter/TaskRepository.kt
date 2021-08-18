package com.kobietka.taskmanagement.repository.inter

import com.kobietka.taskmanagement.data.entity.TaskEntity
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable


interface TaskRepository {
    fun insert(taskEntity: TaskEntity): Completable
    fun getAllByProjectId(projectId: Int): Observable<List<TaskEntity>>
    fun deleteById(id: Int): Completable
    fun getById(id: Int): Maybe<TaskEntity>
    fun deleteFromProjectByProjectId(projectId: Int, taskId: Int): Completable
    fun changeArchivedStatus(taskId: Int, archived: Boolean): Completable
    fun deleteAllTasksFromProject(projectId: Int): Completable
}