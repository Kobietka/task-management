package com.kobietka.taskmanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.kobietka.taskmanagement.ui.screen.*
import com.kobietka.taskmanagement.ui.theme.TaskManagementTheme
import com.kobietka.taskmanagement.ui.theme.orange
import com.kobietka.taskmanagement.ui.util.Route
import com.kobietka.taskmanagement.viewmodel.MainViewModel
import com.kobietka.taskmanagement.viewmodel.ProjectsViewModel
import com.kobietka.taskmanagement.viewmodel.TasksViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private val projectsViewModel: ProjectsViewModel by viewModels()
    private val tasksViewModel: TasksViewModel by viewModels()

    @ExperimentalFoundationApi
    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.loadData()

        setContent {
            val projects = mainViewModel.projects().observeAsState(listOf())
            val navController = rememberNavController()
            TaskManagementTheme {
                NavHost(navController = navController, startDestination = Route.main) {
                    composable(Route.main){
                        window.statusBarColor = MaterialTheme.colors.primary.toArgb()
                        MainScreen(projects, navController)
                    }
                    composable(Route.createProject){
                        window.statusBarColor = Color.White.toArgb()
                        CreateProjectScreen(projectsViewModel, navController)
                    }
                    composable(Route.projectDetails, arguments = listOf(navArgument("id") { type = NavType.IntType })){
                        ProjectDetailsScreen(
                            projectId = it.arguments?.getInt("id", -1)!!,
                            projectsViewModel = projectsViewModel,
                            navController = navController
                        )
                    }
                    composable(Route.createTask, arguments = listOf(navArgument("id") { type = NavType.IntType })){
                        CreateTaskScreen(
                            projectId = it.arguments?.getInt("id", -1)!!,
                            tasksViewModel = tasksViewModel,
                            navController = navController
                        )
                    }
                    composable(Route.taskDetails, arguments = listOf(navArgument("id") { type = NavType.IntType })){
                        TaskDetailsScreen(
                            taskId = it.arguments?.getInt("id", -1)!!,
                            tasksViewModel = tasksViewModel,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}




















