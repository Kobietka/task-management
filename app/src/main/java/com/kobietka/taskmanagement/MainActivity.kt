package com.kobietka.taskmanagement

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.kobietka.taskmanagement.ui.screen.*
import com.kobietka.taskmanagement.ui.theme.TaskManagementTheme
import com.kobietka.taskmanagement.ui.util.Route
import com.kobietka.taskmanagement.viewmodel.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val projectsViewModel: ProjectsViewModel by viewModels()
    private val tasksViewModel: TasksViewModel by viewModels()
    private val timeMeasureViewModel: TimeMeasureViewModel by viewModels()
    private val statusChangeViewModel: StatusChangeViewModel by viewModels()
    private val projectDetailsViewModel: ProjectDetailsViewModel by viewModels()
    private val mainScreenViewModel: MainScreenViewModel by viewModels()
    private val taskDetailsViewModel: TaskDetailsViewModel by viewModels()
    private val createTasksViewModel: CreateTaskViewModel by viewModels()

    @ExperimentalMaterialApi
    @ExperimentalFoundationApi
    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            TaskManagementTheme {
                NavHost(navController = navController, startDestination = Route.main) {
                    composable(Route.main){
                        window.statusBarColor = MaterialTheme.colors.primary.toArgb()
                        mainScreenViewModel.loadProjects()
                        MainScreen(
                            mainScreenViewModel = mainScreenViewModel,
                            navController = navController
                        )
                    }
                    composable(Route.createProject){
                        window.statusBarColor = Color.White.toArgb()
                        CreateProjectScreen(
                            projectsViewModel = projectsViewModel,
                            navController = navController
                        )
                    }
                    composable(Route.projectDetails, arguments = listOf(navArgument("id") { type = NavType.IntType })){
                        window.statusBarColor = MaterialTheme.colors.primary.toArgb()
                        projectDetailsViewModel.loadProjectData(
                            projectId = it.arguments?.getInt("id", -1)!!
                        )
                        statusChangeViewModel.loadStatusEventsData(
                            projectId = it.arguments?.getInt("id", -1)!!
                        )
                        tasksViewModel.loadTaskStatusesData()
                        ProjectDetailsScreen(
                            tasksViewModel = tasksViewModel,
                            projectDetailsViewModel = projectDetailsViewModel,
                            statusChangeViewModel = statusChangeViewModel,
                            navController = navController
                        )
                    }
                    composable(Route.createTask, arguments = listOf(navArgument("id") { type = NavType.IntType })){
                        window.statusBarColor = Color.White.toArgb()
                        createTasksViewModel.loadStatuses(
                            projectId = it.arguments?.getInt("id", -1)!!
                        )
                        CreateTaskScreen(
                            createTaskViewModel = createTasksViewModel,
                            tasksViewModel = tasksViewModel,
                            navController = navController,
                            onDateClick = {
                                showDatePicker(createTaskViewModel = createTasksViewModel)
                            }
                        )
                    }
                    composable(Route.taskDetails, arguments = listOf(navArgument("id") { type = NavType.IntType })){
                        window.statusBarColor = Color.White.toArgb()
                        taskDetailsViewModel.loadTaskData(
                            taskId = it.arguments?.getInt("id", -1)!!
                        )
                        TaskDetailsScreen(
                            taskDetailsViewModel = taskDetailsViewModel,
                            tasksViewModel = tasksViewModel,
                            navController = navController,
                            onDateClick = {
                                showDatePicker(taskDetailsViewModel = taskDetailsViewModel)
                            }
                        )
                    }
                    composable(Route.editProject, arguments = listOf(navArgument("id") { type = NavType.IntType })){
                        window.statusBarColor = Color.White.toArgb()
                        EditProjectScreen(
                            projectId = it.arguments?.getInt("id", -1)!!,
                            projectsViewModel = projectsViewModel,
                            navController = navController
                        )
                    }
                    composable(Route.measureTime, arguments = listOf(navArgument("id") { type = NavType.IntType })){
                        window.statusBarColor = MaterialTheme.colors.primary.toArgb()
                        TaskTimeMeasureScreen(
                            taskId = it.arguments?.getInt("id", -1)!!,
                            tasksViewModel = tasksViewModel,
                            timeMeasureViewModel = timeMeasureViewModel,
                            navController = navController
                        )
                    }
                }
            }
        }
    }

    private fun showDatePicker(createTaskViewModel: CreateTaskViewModel){
        val picker = MaterialDatePicker.Builder.datePicker().build()
        this.let {
            picker.show(it.supportFragmentManager, picker.toString())
            picker.addOnPositiveButtonClickListener {
                createTaskViewModel.setDate(picker.headerText)
            }
        }
    }

    private fun showDatePicker(taskDetailsViewModel: TaskDetailsViewModel){
        val picker = MaterialDatePicker.Builder.datePicker().build()
        this.let {
            picker.show(it.supportFragmentManager, picker.toString())
            picker.addOnPositiveButtonClickListener {
                taskDetailsViewModel.setDate(picker.headerText)
            }
        }
    }

}




















