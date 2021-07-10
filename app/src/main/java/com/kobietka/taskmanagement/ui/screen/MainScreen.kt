package com.kobietka.taskmanagement.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kobietka.taskmanagement.data.ProjectEntity
import com.kobietka.taskmanagement.domain.model.ProjectData
import com.kobietka.taskmanagement.ui.theme.orange
import com.kobietka.taskmanagement.ui.util.Route


@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun MainScreen(projects: State<List<ProjectData>>, navController: NavController){
    Scaffold(
        backgroundColor = MaterialTheme.colors.primary,
        floatingActionButton = {
            FloatingActionButton(backgroundColor = MaterialTheme.colors.primary, onClick = { navController.navigate(Route.createProject) }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "add project", tint = Color.Black)
            }
        },
        topBar = {
            Column(Modifier.padding(20.dp)) {
                Text(text = "Hi, John", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(text = "You have got some work to do!", fontSize = 16.sp, color = Color.Black)
            }
        },
        content = {
            Column(
                Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                    .background(MaterialTheme.colors.secondary)) {
                Column(modifier = Modifier.padding(start = 20.dp, top = 30.dp, end = 20.dp, bottom = 20.dp)) {
                    Text(text = "Your projects", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    LazyColumn(modifier = Modifier.padding(top = 30.dp).fillMaxSize()) {
                        items(projects.value.size){
                            Project(projectData = projects.value[it], navController = navController)
                        }
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
    Card(
        elevation = 4.dp,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
        .padding(bottom = 10.dp)
        .fillMaxWidth()
        .combinedClickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = { navController.navigate(Route.projectDetailsRoute(projectData.projectEntity.id!!)) },
            onLongClick = { navController.navigate(Route.editProjectRoute(projectData.projectEntity.id!!)) }
        )){

        Row(modifier = Modifier.padding(20.dp)){
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically)
                {
                    Text(text = projectData.projectEntity.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(text = "${projectData.numberOfTasks} tasks", fontSize = 16.sp)
                }
                Text(modifier = Modifier.padding(top = 10.dp), text = projectData.projectEntity.description, fontSize = 18.sp)
            }
        }
    }
}
