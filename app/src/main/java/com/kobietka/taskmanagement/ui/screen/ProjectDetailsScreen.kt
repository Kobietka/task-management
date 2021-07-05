package com.kobietka.taskmanagement.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
        floatingActionButton = {
            FloatingActionButton(onClick = { }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "add task")
            }
        },
        content = {
            Column(Modifier.padding(20.dp)) {
                Text(modifier = Modifier
                    .fillMaxWidth()
                    .clickable { descriptionVisible.value = !descriptionVisible.value }, text = "Welcome to ${name.value}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                AnimatedVisibility(visible = descriptionVisible.value) {
                    Text(text = description.value, fontSize = 18.sp, color = Color.Gray)
                }
                Text(text = "Tasks", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                LazyColumn {
                    items(tasks.value.size){
                        Task(taskEntity = tasks.value[it], navController = navController)
                    }
                }
            }
        }
    )
}

@Composable
fun Task(taskEntity: TaskEntity, navController: NavController){

}


















