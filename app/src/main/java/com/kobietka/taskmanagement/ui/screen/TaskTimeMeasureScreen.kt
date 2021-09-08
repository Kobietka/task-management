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
import androidx.compose.material.icons.filled.History
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
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
import com.kobietka.taskmanagement.data.entity.TaskSessionEntity
import com.kobietka.taskmanagement.ui.theme.indigo
import com.kobietka.taskmanagement.ui.util.Route
import com.kobietka.taskmanagement.viewmodel.TasksViewModel
import com.kobietka.taskmanagement.viewmodel.TimeMeasureViewModel


@ExperimentalAnimationApi
@Composable
fun TaskTimeMeasureScreen(
    tasksViewModel: TasksViewModel,
    timeMeasureViewModel: TimeMeasureViewModel,
    navController: NavController
){
    val taskName = timeMeasureViewModel.taskName().observeAsState(initial = "")
    val projectId = timeMeasureViewModel.projectId().observeAsState(initial = 0)
    val taskId = timeMeasureViewModel.taskId().observeAsState(initial = 0)
    val timeText = timeMeasureViewModel.timeText().observeAsState(initial = "00:00")
    val time = timeMeasureViewModel.time().observeAsState(initial = 0)
    val sessions = timeMeasureViewModel.taskSessions().observeAsState(initial = listOf())
    val timerRunning = timeMeasureViewModel.isTimerRunning().observeAsState(initial = false)
    val loadingFinished = timeMeasureViewModel.isLoadingFinished().observeAsState(initial = false)

    val firstTime = remember { mutableStateOf(true) }

    val sessionsVisible = remember { mutableStateOf(false) }
    val switchEnabled = remember { mutableStateOf(true) }

    if(firstTime.value && loadingFinished.value){
        if(!timerRunning.value) timeMeasureViewModel.resetTimer()
        firstTime.value = false
    }

    Scaffold(
        backgroundColor = MaterialTheme.colors.primary,
        topBar = {
            Column {
                Row(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Column(modifier = Modifier.fillMaxWidth(0.85f)) {
                        Text(
                            text = "Measure time of",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                        Text(
                            text = taskName.value,
                            fontSize = 20.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    AnimatedVisibility(visible = sessions.value.isNotEmpty()) {
                        Icon(
                            modifier = Modifier
                                .size(30.dp, 30.dp)
                                .clickable {
                                    sessionsVisible.value = !sessionsVisible.value
                                },
                            imageVector = Icons.Filled.History,
                            contentDescription = "session history",
                            tint = Color.Black
                        )
                    }
                }
                AnimatedVisibility(visible = sessionsVisible.value) {
                    Column(Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp)) {
                        Text(
                            text = "Past sessions",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 18.sp
                        )
                        LazyColumn {
                            items(sessions.value.size){
                                Session(sessionEntity = sessions.value[it])
                            }
                        }
                    }
                }
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .padding(30.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Mark task as completed",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Switch(
                        checked = switchEnabled.value,
                        onCheckedChange = { switchEnabled.value = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = indigo)
                    )
                }

                Text(
                    text = timeText.value,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                    fontSize = 60.sp
                )

                Button(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                        .height(60.dp),
                    onClick = {
                        if(!timerRunning.value) {
                            timeMeasureViewModel.startTimer(taskId = taskId.value)
                        } else {
                            timeMeasureViewModel.pauseTimer()
                            timeMeasureViewModel.saveSession(
                                taskId = taskId.value,
                                projectId = projectId.value,
                                timeInSeconds = time.value
                            )
                            if(switchEnabled.value){
                                tasksViewModel.changeTaskStatusToCompleted(
                                    taskId = taskId.value,
                                    onFinish = {
                                        navController.navigate(Route.projectDetailsRoute(projectId.value)) {
                                            popUpTo(Route.projectDetails) {
                                                inclusive = true
                                            }
                                        }
                                    }
                                )
                            } else navController.navigate(Route.projectDetailsRoute(projectId.value)) {
                                popUpTo(Route.projectDetails) {
                                    inclusive = true
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.secondary
                    )
                ) {
                    Text(
                        text = if(timerRunning.value) "End session" else "Start session",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
                
            }
        }
    )
}


@Composable
fun Session(sessionEntity: TaskSessionEntity){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = sessionEntity.date,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            fontSize = 15.sp
        )
        Text(
            text = "${sessionEntity.timeInSeconds/60} minutes",
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            fontSize = 15.sp
        )
    }
}




