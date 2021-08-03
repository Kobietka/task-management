package com.kobietka.taskmanagement.ui.util



class Route {
    companion object {
        const val main = "main"
        const val createProject = "create_project"
        const val projectDetails = "project/{id}"
        const val createTask = "create_task/{id}"
        const val taskDetails = "task/{id}"
        const val editProject = "edit_project/{id}"
        const val measureTime = "time/{id}"

        fun projectDetailsRoute(id: Int): String {
            return "project/$id"
        }

        fun createTaskRoute(projectId: Int): String {
            return "create_task/$projectId"
        }

        fun taskDetailsRoute(taskId: Int): String {
            return "task/$taskId"
        }

        fun editProjectRoute(projectId: Int): String {
            return "edit_project/$projectId"
        }

        fun measureTimeRoute(taskId: Int): String {
            return "time/$taskId"
        }
    }
}
