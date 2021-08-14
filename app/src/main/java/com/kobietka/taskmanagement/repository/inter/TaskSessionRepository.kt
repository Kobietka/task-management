package com.kobietka.taskmanagement.repository.inter

import com.kobietka.taskmanagement.data.entity.TaskSessionEntity
import io.reactivex.Completable
import io.reactivex.Maybe

interface TaskSessionRepository {
    fun insert(taskSessionEntity: TaskSessionEntity): Completable
    fun getAllByTaskId(taskId: Int): Maybe<List<TaskSessionEntity>>
    fun deleteById(id: Int): Completable
    fun getAllByProjectId(projectId: Int): Maybe<List<TaskSessionEntity>>
}