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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kobietka.taskmanagement.data.ProjectEntity
import com.kobietka.taskmanagement.ui.util.Route
import com.kobietka.taskmanagement.viewmodel.MainViewModel


@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun MainScreen(mainViewModel: MainViewModel, navController: NavController){
    val projects = remember { mutableStateOf<List<ProjectEntity>>(listOf()) }
    val lazyColumnState = rememberLazyListState()
    val topAppBarVisible = remember { mutableStateOf(true) }
    val infoVisible = remember { mutableStateOf(false) }

    mainViewModel.loadProjects {
        projects.value = it
    }

    topAppBarVisible.value = !lazyColumnState.isScrollInProgress

    Scaffold(
        backgroundColor = MaterialTheme.colors.primary,
        floatingActionButton = {
            FloatingActionButton(backgroundColor = MaterialTheme.colors.primary, onClick = { navController.navigate(Route.createProject) }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "add project", tint = Color.Black)
            }
        },
        topBar = {
            AnimatedVisibility(visible = topAppBarVisible.value) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                        Column {
                            Text(text = "Hi!", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            Text(text = "You have got some work to do!", fontSize = 16.sp, color = Color.Black)
                        }
                        Icon(modifier = Modifier
                            .size(30.dp, 30.dp)
                            .clickable { infoVisible.value = !infoVisible.value }, imageVector = Icons.Outlined.Info, contentDescription = "info", tint = Color.Black)
                    }
                    AnimatedVisibility(visible = infoVisible.value) {
                        Card(modifier = Modifier.padding(10.dp), backgroundColor = Color.White) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(text = "To create project click + button.")
                                Text(text = "Click on project to see tasks.")
                                Text(text = "Long click on project to edit it.")
                            }
                        }
                    }
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
                        Text(modifier = Modifier.padding(top = 32.dp, start = 20.dp, end = 20.dp), text = "Your projects", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                    if(projects.value.isEmpty()){
                        Row(modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            Text(text = "No projects", color = Color.Black, fontWeight = FontWeight.Medium, fontSize = 17.sp)
                        }
                    } else {
                        LazyColumn(modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp, top = 5.dp)
                            .fillMaxSize(), state = lazyColumnState) {
                            items(projects.value.size){
                                Project(projectEntity = projects.value[it], navController = navController)
                            }
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
fun Project(projectEntity: ProjectEntity, navController: NavController){
    Card(
        elevation = 4.dp,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .padding(bottom = 10.dp)
            .fillMaxWidth()
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
                onClick = { navController.navigate(Route.projectDetailsRoute(projectEntity.id!!)) },
                onLongClick = { navController.navigate(Route.editProjectRoute(projectEntity.id!!)) }
            )){

        Row(modifier = Modifier.padding(20.dp)){
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically)
                {
                    Text(text = projectEntity.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                Text(modifier = Modifier.padding(top = 10.dp), text = projectEntity.description, fontSize = 18.sp)
            }
        }
    }
}
