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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.kobietka.taskmanagement.ui.screen.*
import com.kobietka.taskmanagement.ui.theme.TaskManagementTheme
import com.kobietka.taskmanagement.ui.util.Route
import com.kobietka.taskmanagement.viewmodel.ProjectsViewModel
import com.kobietka.taskmanagement.viewmodel.StatusChangeViewModel
import com.kobietka.taskmanagement.viewmodel.TasksViewModel
import com.kobietka.taskmanagement.viewmodel.TimeMeasureViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val projectsViewModel: ProjectsViewModel by viewModels()
    private val tasksViewModel: TasksViewModel by viewModels()
    private val timeMeasureViewModel: TimeMeasureViewModel by viewModels()
    private val statusChangeViewModel: StatusChangeViewModel by viewModels()

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
                        MainScreen(projectsViewModel, navController)
                    }
                    composable(Route.createProject){
                        window.statusBarColor = Color.White.toArgb()
                        CreateProjectScreen(projectsViewModel, navController)
                    }
                    composable(Route.projectDetails, arguments = listOf(navArgument("id") { type = NavType.IntType })){
                        window.statusBarColor = MaterialTheme.colors.primary.toArgb()
                        ProjectDetailsScreen(
                            projectId = it.arguments?.getInt("id", -1)!!,
                            projectsViewModel = projectsViewModel,
                            navController = navController,
                            tasksViewModel = tasksViewModel,
                            statusChangeViewModel = statusChangeViewModel,
                            timeMeasureViewModel = timeMeasureViewModel
                        )
                    }
                    composable(Route.createTask, arguments = listOf(navArgument("id") { type = NavType.IntType })){
                        window.statusBarColor = Color.White.toArgb()
                        CreateTaskScreen(
                            projectId = it.arguments?.getInt("id", -1)!!,
                            tasksViewModel = tasksViewModel,
                            navController = navController,
                            onDateClick = {
                                showDatePicker()
                            }
                        )
                    }
                    composable(Route.taskDetails, arguments = listOf(navArgument("id") { type = NavType.IntType })){
                        window.statusBarColor = Color.White.toArgb()
                        TaskDetailsScreen(
                            taskId = it.arguments?.getInt("id", -1)!!,
                            tasksViewModel = tasksViewModel,
                            navController = navController,
                            onDateClick = {
                                showDatePicker()
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

    private fun showDatePicker(){
        val picker = MaterialDatePicker.Builder.datePicker().build()
        this.let {
            picker.show(it.supportFragmentManager, picker.toString())
            picker.addOnPositiveButtonClickListener {
                tasksViewModel.setDate(picker.headerText)
            }
        }
    }
}




















