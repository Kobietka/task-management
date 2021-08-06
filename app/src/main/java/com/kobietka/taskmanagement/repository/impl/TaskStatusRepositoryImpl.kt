package com.kobietka.taskmanagement.repository.impl

import com.kobietka.taskmanagement.data.dao.TaskStatusDao
import com.kobietka.taskmanagement.data.entity.TaskStatusEntity
import com.kobietka.taskmanagement.repository.inter.TaskStatusRepository
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import javax.inject.Inject

class TaskStatusRepositoryImpl
@Inject constructor(private val taskStatusDao: TaskStatusDao): TaskStatusRepository {

    override fun insert(taskStatusEntity: TaskStatusEntity): Completable {
        return taskStatusDao.insert(taskStatusEntity)
    }

    override fun getById(id: Int): Maybe<TaskStatusEntity> {
        return taskStatusDao.getById(id)
    }

    override fun deleteById(id: Int): Completable {
        return taskStatusDao.deleteById(id)
    }

    override fun getAll(): Maybe<List<TaskStatusEntity>> {
        return taskStatusDao.getAll()
    }

    override fun getAllObservable(): Observable<List<TaskStatusEntity>> {
        return taskStatusDao.getAllObservable()
    }
}