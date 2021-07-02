package com.kobietka.taskmanagement.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kobietka.taskmanagement.data.ProjectEntity
import com.kobietka.taskmanagement.ui.util.Route


@ExperimentalAnimationApi
@Composable
fun MainScreen(projects: State<List<ProjectEntity>>, navController: NavController){
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Route.createProject) }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "add project")
            }
        },
        content = {
            LazyColumn {
                items(projects.value.size){
                    Project(projectEntity = projects.value[it])
                }
            }
        }
    )
}

@ExperimentalAnimationApi
@Composable
fun Project(projectEntity: ProjectEntity){
    val expanded = remember { mutableStateOf(false) }
    Card(modifier = Modifier.fillMaxWidth().clickable { expanded.value = !expanded.value }) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(text = projectEntity.name)
            AnimatedVisibility(visible = expanded.value) {
                Text(text = projectEntity.description)
            }
        }
    }
}