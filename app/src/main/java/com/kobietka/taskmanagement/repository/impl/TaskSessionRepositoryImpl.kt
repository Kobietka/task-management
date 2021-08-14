package com.kobietka.taskmanagement.repository.impl

import com.kobietka.taskmanagement.data.dao.TaskSessionDao
import com.kobietka.taskmanagement.data.entity.TaskSessionEntity
import com.kobietka.taskmanagement.repository.inter.TaskSessionRepository
import io.reactivex.Completable
import io.reactivex.Maybe
import javax.inject.Inject


class TaskSessionRepositoryImpl
@Inject constructor(private val taskSessionDao: TaskSessionDao): TaskSessionRepository {

    override fun insert(taskSessionEntity: TaskSessionEntity): Completable {
        return taskSessionDao.insert(taskSessionEntity)
    }

    override fun getAllByTaskId(taskId: Int): Maybe<List<TaskSessionEntity>> {
        return taskSessionDao.getAllByTaskId(taskId)
    }

    override fun deleteById(id: Int): Completable {
        return taskSessionDao.deleteById(id)
    }

    override fun getAllByProjectId(projectId: Int): Maybe<List<TaskSessionEntity>> {
        return taskSessionDao.getAllByProjectId(projectId)
    }

}