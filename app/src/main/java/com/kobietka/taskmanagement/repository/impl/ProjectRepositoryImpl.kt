package com.kobietka.taskmanagement.repository.impl

import com.kobietka.taskmanagement.data.ProjectDao
import com.kobietka.taskmanagement.data.ProjectEntity
import com.kobietka.taskmanagement.repository.inter.ProjectRepository
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject


class ProjectRepositoryImpl
@Inject constructor(private val projectDao: ProjectDao): ProjectRepository {

    override fun insert(projectEntity: ProjectEntity): Completable {
        return projectDao.insert(projectEntity)
    }

    override fun getAll(): Observable<List<ProjectEntity>> {
        return projectDao.getAll()
    }

    override fun deleteById(id: Int): Completable {
        return projectDao.deleteById(id)
    }

    override fun getById(id: Int): Maybe<ProjectEntity> {
        return projectDao.getById(id)
    }

}