package com.kobietka.taskmanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kobietka.taskmanagement.ui.screen.CreateProjectScreen
import com.kobietka.taskmanagement.ui.screen.MainScreen
import com.kobietka.taskmanagement.ui.theme.TaskManagementTheme
import com.kobietka.taskmanagement.ui.util.Route
import com.kobietka.taskmanagement.viewmodel.MainViewModel
import com.kobietka.taskmanagement.viewmodel.ProjectsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private val projectsViewModel: ProjectsViewModel by viewModels()

    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.loadData()

        setContent {
            val projects = mainViewModel.projects().observeAsState(listOf())
            val navController = rememberNavController()
            TaskManagementTheme {
                Surface(color = MaterialTheme.colors.background) {
                    NavHost(navController = navController, startDestination = Route.main) {
                        composable(Route.main){
                            MainScreen(projects, navController)
                        }
                        composable(Route.createProject){
                            CreateProjectScreen(projectsViewModel, navController)
                        }
                    }
                }
            }
        }
    }
}


