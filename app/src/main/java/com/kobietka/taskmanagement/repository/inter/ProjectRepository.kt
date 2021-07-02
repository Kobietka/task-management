package com.kobietka.taskmanagement.repository.inter

import com.kobietka.taskmanagement.data.ProjectEntity
import io.reactivex.Completable
import io.reactivex.Observable


interface ProjectRepository {
    fun insert(projectEntity: ProjectEntity): Completable
    fun getAll(): Observable<List<ProjectEntity>>
    fun deleteById(id: Int): Completable
}