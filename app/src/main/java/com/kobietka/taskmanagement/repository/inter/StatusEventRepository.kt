package com.kobietka.taskmanagement.repository.inter

import com.kobietka.taskmanagement.data.StatusEventEntity
import io.reactivex.Completable
import io.reactivex.Maybe

interface StatusEventRepository {
    fun insert(statusEventEntity: StatusEventEntity): Completable
    fun getAll(): Maybe<List<StatusEventEntity>>
    fun getAllByProject(projectId: Int): Maybe<List<StatusEventEntity>>
}