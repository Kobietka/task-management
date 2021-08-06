package com.kobietka.taskmanagement.repository.impl

import com.kobietka.taskmanagement.data.dao.TaskDao
import com.kobietka.taskmanagement.data.entity.TaskEntity
import com.kobietka.taskmanagement.repository.inter.TaskRepository
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import javax.inject.Inject


class TaskRepositoryImpl
@Inject constructor(private val taskDao: TaskDao): TaskRepository {

    override fun insert(taskEntity: TaskEntity): Completable {
        return taskDao.insert(taskEntity)
    }

    override fun getAllByProjectId(projectId: Int): Observable<List<TaskEntity>> {
        return taskDao.getAllByProjectId(projectId)
    }

    override fun deleteById(id: Int): Completable {
        return taskDao.deleteById(id)
    }

    override fun getById(id: Int): Maybe<TaskEntity> {
        return taskDao.getById(id)
    }

    override fun deleteFromProjectByProjectId(projectId: Int, taskId: Int): Completable {
        return taskDao.deleteFromProjectByProjectId(projectId, taskId)
    }

    override fun changeArchivedStatus(taskId: Int, archived: Boolean): Completable {
        return taskDao.changeArchivedStatus(taskId, archived)
    }

}