package com.kobietka.taskmanagement.ui.screen

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kobietka.taskmanagement.data.TaskEntity
import com.kobietka.taskmanagement.data.TaskStatusEntity
import com.kobietka.taskmanagement.ui.theme.indigo
import com.kobietka.taskmanagement.ui.util.Route
import com.kobietka.taskmanagement.viewmodel.ProjectsViewModel
import com.kobietka.taskmanagement.viewmodel.TasksViewModel


@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun ProjectDetailsScreen(projectId: Int, projectsViewModel: ProjectsViewModel, navController: NavController, tasksViewModel: TasksViewModel){
    val name = remember { mutableStateOf("") }
    val descriptionVisible = remember { mutableStateOf(false) }
    val description = remember { mutableStateOf("") }
    val tasks = remember { mutableStateOf(listOf<TaskEntity>()) }
    val firstTime = remember { mutableStateOf(true) }
    val statuses = remember { mutableStateOf(listOf<TaskStatusEntity>()) }

    val taskListFilter = remember { mutableStateOf("no filter") }

    val filterMenuExpanded = remember { mutableStateOf(false) }

    if(firstTime.value){
        projectsViewModel.loadProjectWithTasks(
            id = projectId,
            onFinish = { project, projectTasks ->
                name.value = project.name
                description.value = project.description
                tasks.value = projectTasks
            }
        )
        tasksViewModel.loadTaskStatuses { statusList ->
            statuses.value = statusList
        }
        firstTime.value = !firstTime.value
    }

    Scaffold(
        backgroundColor = MaterialTheme.colors.primary,
        floatingActionButton = {
            FloatingActionButton(
                backgroundColor = MaterialTheme.colors.primary,
                onClick = { navController.navigate(Route.createTaskRoute(projectId)) }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "add task", tint = Color.Black)
            }
        },
        topBar = {
            Column(Modifier.padding(20.dp)) {
                Text(text = "Welcome to", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                AnimatedVisibility(visible = name.value != "") {
                    Text(modifier = Modifier.clickable { descriptionVisible.value = !descriptionVisible.value }, text = name.value, fontSize = 20.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                }
                AnimatedVisibility(visible = descriptionVisible.value) {
                    Text(modifier = Modifier.padding(top = 5.dp), text = description.value, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                }
            }
        },
        content = {
            Column(
                Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                    .background(MaterialTheme.colors.secondary)) {
                Column {
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp), backgroundColor = MaterialTheme.colors.secondary, elevation = 20.dp) {
                        Row(modifier = Modifier.padding(top = 32.dp, start = 20.dp, end = 20.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Tasks", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            Icon(
                                modifier = Modifier
                                    .size(30.dp, 30.dp)
                                    .clickable {
                                        filterMenuExpanded.value = !filterMenuExpanded.value
                                    },
                                imageVector = Icons.Filled.FilterAlt,
                                contentDescription = "filter",
                                tint = Color.Black
                            )
                        }
                        DropdownMenu(expanded = filterMenuExpanded.value, onDismissRequest = { filterMenuExpanded.value = false }, offset = DpOffset(270.dp, 0.dp)) {
                            DropdownMenuItem(onClick = { taskListFilter.value = "no filter"; filterMenuExpanded.value = false }) {
                                Text(text = "No filter")
                            }
                            DropdownMenuItem(onClick = { taskListFilter.value = "by status"; filterMenuExpanded.value = false }) {
                                Text(text = "By status")
                            }
                            statuses.value.forEach { taskStatus ->
                                DropdownMenuItem(onClick = { taskListFilter.value = taskStatus.name; filterMenuExpanded.value = false }) {
                                    Text(text = taskStatus.name.firstCapital())
                                }
                            }
                        }
                    }
                    when(taskListFilter.value){
                        "no filter" -> {
                            LazyColumn(modifier = Modifier
                                .padding(start = 20.dp, end = 20.dp, top = 5.dp)
                                .fillMaxSize()) {
                                items(tasks.value.size){
                                    Task(taskEntity = tasks.value[it], navController = navController, projectsViewModel = projectsViewModel)
                                }
                            }
                        }
                        "by status" -> {
                            TaskListByStatus(
                                statuses = statuses.value,
                                tasks = tasks.value,
                                navController = navController,
                                projectsViewModel = projectsViewModel
                            )
                        }
                        else -> {
                            statuses.value.forEach { taskStatus ->
                                if(taskStatus.name == taskListFilter.value){
                                    TaskListWithOnlyOneStatus(
                                        statuses = statuses.value,
                                        statusId = taskStatus.id!!,
                                        tasks = tasks.value,
                                        navController = navController,
                                        projectsViewModel = projectsViewModel
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@ExperimentalAnimationApi
@Composable
fun Task(taskEntity: TaskEntity, projectsViewModel: ProjectsViewModel, navController: NavController){
    val status = remember { mutableStateOf("") }
    val firstTime = remember { mutableStateOf(true) }

    if(firstTime.value){
        projectsViewModel.loadTaskStatus(
            statusId = taskEntity.statusId,
            onFinish = { taskStatusEntity ->
                status.value = taskStatusEntity.name
            }
        )
    }

    Card(shape = RoundedCornerShape(10.dp), modifier = Modifier
        .padding(bottom = 10.dp)
        .clickable { navController.navigate(Route.taskDetailsRoute(taskEntity.id!!)) }) {
        Column(modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()) {
            Text(modifier = Modifier.padding(bottom = 10.dp), text = taskEntity.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            AnimatedVisibility(visible = status.value != "") {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .background(
                            when (status.value) {
                                "Not started" -> statusRed()
                                "In progress" -> statusBlue()
                                "Completed" -> statusGreen()
                                else -> Color.Black
                            }
                        )
                ) {
                    Text(
                        modifier = Modifier.padding(
                            start = 20.dp,
                            top = 10.dp,
                            end = 20.dp,
                            bottom = 10.dp
                        ),
                        text = status.value,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun TaskListByStatus(statuses: List<TaskStatusEntity>, tasks: List<TaskEntity>, navController: NavController, projectsViewModel: ProjectsViewModel){
    val scrollState = rememberScrollState()

    Column(
        Modifier
            .verticalScroll(scrollState, true)
            .padding(start = 20.dp, end = 20.dp, top = 5.dp)) {
        statuses.forEach { status ->
            val tasksWithStatus = tasks.filter { task -> task.statusId == status.id }
            Text(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp), text = status.name, color = Color.Black, fontWeight = FontWeight.Medium, fontSize = 17.sp)
            tasksWithStatus.forEach {
                Task(taskEntity = it, navController = navController, projectsViewModel = projectsViewModel)
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun TaskListWithOnlyOneStatus(statuses: List<TaskStatusEntity>, statusId: Int, tasks: List<TaskEntity>, navController: NavController, projectsViewModel: ProjectsViewModel){
    val filteredTasks = tasks.filter { task -> task.statusId == statusId }
    val status = statuses.first { taskStatus -> taskStatus.id == statusId }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp, start = 20.dp), text = status.name, color = Color.Black, fontWeight = FontWeight.Medium, fontSize = 17.sp)
        LazyColumn(modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 5.dp)
            .fillMaxSize()) {
            items(filteredTasks.size){
                Task(taskEntity = filteredTasks[it], navController = navController, projectsViewModel = projectsViewModel)
            }
        }
    }
}


fun statusBlue(): Color {
    return Color(0xFFB3E5FC)
}

fun statusRed(): Color {
    return Color(0xFFFF8A65)
}

fun statusGreen(): Color {
    return Color(0xFF81C784)
}

fun String.firstCapital(): String {
    return this.first().uppercase() + this.substring(1)
}















