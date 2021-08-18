package com.kobietka.taskmanagement.repository.impl

import com.kobietka.taskmanagement.data.dao.StatusEventDao
import com.kobietka.taskmanagement.data.entity.StatusEventEntity
import com.kobietka.taskmanagement.repository.inter.StatusEventRepository
import io.reactivex.Completable
import io.reactivex.Maybe
import javax.inject.Inject


class StatusEventRepositoryImpl
@Inject constructor(private val statusEventDao: StatusEventDao): StatusEventRepository {

    override fun insert(statusEventEntity: StatusEventEntity): Completable {
        return statusEventDao.insert(statusEventEntity)
    }

    override fun getAll(): Maybe<List<StatusEventEntity>> {
        return statusEventDao.getAll()
    }

    override fun getAllByProject(projectId: Int): Maybe<List<StatusEventEntity>> {
        return statusEventDao.getAllByProject(projectId)
    }

    override fun deleteEventsByProjectId(projectId: Int): Completable {
        return statusEventDao.deleteEventsByProjectId(projectId)
    }
}