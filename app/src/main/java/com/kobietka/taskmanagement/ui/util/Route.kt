package com.kobietka.taskmanagement.ui.util



class Route {
    companion object {
        const val main = "main"
        const val createProject = "create_project"
        const val projectDetails = "project/{id}"

        fun projectDetailsRoute(id: Int): String {
            return "project/$id"
        }
    }
}
