package com.kobietka.taskmanagement.repository.inter

import com.kobietka.taskmanagement.data.entity.ProjectEntity
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable


interface ProjectRepository {
    fun insert(projectEntity: ProjectEntity): Completable
    fun getAll(): Observable<List<ProjectEntity>>
    fun deleteById(id: Int): Completable
    fun getAllMaybe(): Maybe<List<ProjectEntity>>
    fun getById(id: Int): Maybe<ProjectEntity>
    fun getNumberOfTasksInProject(id: Int): Maybe<Int>
}