package com.kobietka.taskmanagement.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kobietka.taskmanagement.data.ProjectEntity
import com.kobietka.taskmanagement.domain.model.ProjectData
import com.kobietka.taskmanagement.ui.util.Route


@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun MainScreen(projects: State<List<ProjectData>>, navController: NavController){
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Route.createProject) }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "add project")
            }
        },
        topBar = {
             Column(Modifier.padding(20.dp)) {
                 Text(text = "Hi, \$name", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                 Text(text = "You have got some work to do!", fontSize = 15.sp, color = Color.Gray)
             }
        },
        content = {
            Column {
                Text(modifier = Modifier.padding(start = 20.dp, top = 10.dp, bottom = 10.dp), text = "Your projects", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                LazyColumn {
                    items(projects.value.size){
                        Project(projectData = projects.value[it], navController = navController)
                    }
                }
            }
        }
    )
}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun Project(projectData: ProjectData, navController: NavController){
    val expanded = remember { mutableStateOf(false) }
    Card(modifier = Modifier
        .fillMaxWidth()
        .combinedClickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = { navController.navigate(Route.projectDetailsRoute(projectData.projectEntity.id!!)) },
            onLongClick = { expanded.value = !expanded.value }
        )){
        Row {
            Column(modifier = Modifier.padding(start = 20.dp, top = 10.dp, end = 20.dp, bottom = 10.dp)) {
                Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = projectData.projectEntity.name, fontWeight = FontWeight.Medium, fontSize = 18.sp)
                    AnimatedVisibility(visible = expanded.value) {
                        Text(text = "${projectData.numberOfTasks} tasks")
                    }
                }
                AnimatedVisibility(visible = expanded.value) {
                    Text(text = projectData.projectEntity.description, fontSize = 16.sp, color = Color.Gray)
                }
            }
        }
    }
}
