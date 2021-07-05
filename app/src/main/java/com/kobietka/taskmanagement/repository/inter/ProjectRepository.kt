package com.kobietka.taskmanagement.repository.inter

import com.kobietka.taskmanagement.data.ProjectEntity
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single


interface ProjectRepository {
    fun insert(projectEntity: ProjectEntity): Completable
    fun getAll(): Observable<List<ProjectEntity>>
    fun deleteById(id: Int): Completable
    fun getById(id: Int): Maybe<ProjectEntity>
    fun getNumberOfTasksInProject(id: Int): Maybe<Int>
}