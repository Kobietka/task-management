package com.kobietka.taskmanagement

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.kobietka.taskmanagement.ui.screen.*
import com.kobietka.taskmanagement.ui.theme.TaskManagementTheme
import com.kobietka.taskmanagement.ui.theme.green
import com.kobietka.taskmanagement.ui.util.Route
import com.kobietka.taskmanagement.ui.util.UiState
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
    private val editProjectViewModel: EditProjectViewModel by viewModels()

    @ExperimentalMaterialApi
    @ExperimentalFoundationApi
    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val uiState = remember { mutableStateOf(UiState.MainScreen) }
            val isTimerRunning = timeMeasureViewModel.isTimerRunning().observeAsState(initial = false)
            val timerRunningForTask = timeMeasureViewModel.timerRunningForTask().observeAsState(initial = 0)
            TaskManagementTheme {
                Column {
                    if(isTimerRunning.value && uiState.value != UiState.MeasureTime){
                        TimerBar(
                            timeMeasureViewModel = timeMeasureViewModel,
                            navController = navController,
                            runningTaskId = timerRunningForTask.value
                        )
                    }
                    NavHost(navController = navController, startDestination = Route.main) {
                        composable(route = Route.main){
                            uiState.value = UiState.MainScreen
                            window.statusBarColor = MaterialTheme.colors.primary.toArgb()
                            mainScreenViewModel.loadProjects()
                            MainScreen(
                                mainScreenViewModel = mainScreenViewModel,
                                navController = navController
                            )
                        }
                        composable(route = Route.createProject){
                            uiState.value = UiState.CreateProject
                            window.statusBarColor = Color.White.toArgb()
                            CreateProjectScreen(
                                projectsViewModel = projectsViewModel,
                                navController = navController
                            )
                        }
                        composable(
                            route = Route.projectDetails,
                            arguments = listOf(navArgument("id") { type = NavType.IntType })
                        ){
                            uiState.value = UiState.ProjectDetails
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
                                navController = navController,
                                isMeasuringTimeBlocked = isTimerRunning.value
                            )
                        }
                        composable(
                            route = Route.createTask,
                            arguments = listOf(navArgument("id") { type = NavType.IntType })
                        ){
                            uiState.value = UiState.CreateTask
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
                        composable(
                            route = Route.taskDetails,
                            arguments = listOf(navArgument("id") { type = NavType.IntType })
                        ){
                            uiState.value = UiState.TaskDetails
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
                        composable(
                            route = Route.editProject,
                            arguments = listOf(navArgument("id") { type = NavType.IntType })
                        ){
                            uiState.value = UiState.EditProject
                            window.statusBarColor = Color.White.toArgb()
                            editProjectViewModel.loadProjectData(
                                projectId = it.arguments?.getInt("id", -1)!!
                            )
                            EditProjectScreen(
                                editProjectViewModel = editProjectViewModel,
                                projectsViewModel = projectsViewModel,
                                navController = navController
                            )
                        }
                        composable(
                            route = Route.measureTime,
                            arguments = listOf(navArgument("id") { type = NavType.IntType })
                        ){
                            uiState.value = UiState.MeasureTime
                            window.statusBarColor = MaterialTheme.colors.primary.toArgb()
                            timeMeasureViewModel.loadTaskData(
                                taskId = it.arguments?.getInt("id", -1)!!
                            )
                            TaskTimeMeasureScreen(
                                tasksViewModel = tasksViewModel,
                                timeMeasureViewModel = timeMeasureViewModel,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun TimerBar(
        timeMeasureViewModel: TimeMeasureViewModel,
        navController: NavController,
        runningTaskId: Int
    ){
        val taskName = timeMeasureViewModel.taskName().observeAsState(initial = "")
        val time = timeMeasureViewModel.timeText().observeAsState(initial = "")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate(Route.measureTimeRoute(taskId = runningTaskId)) }
                .background(green)
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                    text = taskName.value,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = time.value,
                    fontWeight = FontWeight.Bold
                )
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




















