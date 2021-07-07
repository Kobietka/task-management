package com.kobietka.taskmanagement.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kobietka.taskmanagement.data.TaskEntity
import com.kobietka.taskmanagement.viewmodel.ProjectsViewModel


@ExperimentalAnimationApi
@Composable
fun ProjectDetailsScreen(projectId: Int, projectsViewModel: ProjectsViewModel, navController: NavController){
    val name = remember { mutableStateOf("") }
    val descriptionVisible = remember { mutableStateOf(true) }
    val description = remember { mutableStateOf("") }
    val tasks = remember { mutableStateOf(listOf<TaskEntity>()) }
    val firstTime = remember { mutableStateOf(true) }

    if(firstTime.value){
        projectsViewModel.loadProject(
            id = projectId,
            onFinish = { project, projectTasks ->
                name.value = project.name
                description.value = project.description
                tasks.value = projectTasks
            }
        )
        firstTime.value = !firstTime.value
    }

    Scaffold(
        backgroundColor = MaterialTheme.colors.primary,
        floatingActionButton = {
            FloatingActionButton(backgroundColor = MaterialTheme.colors.primary, onClick = { }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "add task", tint = Color.Black)
            }
        },
        topBar = {
            Column(Modifier.padding(20.dp)) {
                Text(text = "Welcome to", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                Text(text = name.value, fontSize = 20.sp, color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        content = {
            Column(
                Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                    .background(MaterialTheme.colors.secondary)) {
                Column(modifier = Modifier.padding(start = 20.dp, top = 30.dp, end = 20.dp, bottom = 20.dp)) {
                    Text(text = "Tasks", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    LazyColumn(modifier = Modifier
                        .padding(top = 30.dp)
                        .fillMaxSize()) {
                        items(tasks.value.size){
                            Task(taskEntity = tasks.value[it], navController = navController, projectsViewModel = projectsViewModel)
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

    Card(shape = RoundedCornerShape(10.dp)) {
        Column(modifier = Modifier.padding(20.dp).fillMaxWidth()) {
            Text(modifier = Modifier.padding(bottom = 10.dp), text = taskEntity.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            AnimatedVisibility(visible = status.value != "") {
                Column(
                    modifier = Modifier.clip(RoundedCornerShape(5.dp)).background(
                        when(status.value){
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

fun statusBlue(): Color {
    return Color(0xFFB3E5FC)
}

fun statusRed(): Color {
    return Color(0xFFFF8A65)
}

fun statusGreen(): Color {
    return Color(0xFF81C784)
}

















