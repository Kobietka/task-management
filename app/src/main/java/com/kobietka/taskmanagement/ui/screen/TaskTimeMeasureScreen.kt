package com.kobietka.taskmanagement.ui.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import com.kobietka.taskmanagement.ui.theme.indigo
import com.kobietka.taskmanagement.ui.util.Route
import com.kobietka.taskmanagement.viewmodel.TasksViewModel
import com.kobietka.taskmanagement.viewmodel.TimeMeasureViewModel


@ExperimentalAnimationApi
@Composable
fun TaskTimeMeasureScreen(taskId: Int, tasksViewModel: TasksViewModel, timeMeasureViewModel: TimeMeasureViewModel, navController: NavController){

    val taskName = remember { mutableStateOf("") }
    val projectId = remember { mutableStateOf(0) }
    val timeText = timeMeasureViewModel.timeText().observeAsState(initial = "00:00")
    val time = timeMeasureViewModel.time().observeAsState(initial = 0)
    val timerStarted = remember { mutableStateOf(false) }

    val switchEnabled = remember { mutableStateOf(true) }

    tasksViewModel.loadTask(
        taskId = taskId,
        onFinish = { task ->
            taskName.value = task.name
            projectId.value = task.projectId
        }
    )

    Scaffold(
        backgroundColor = MaterialTheme.colors.primary,
        topBar = {
            Column(Modifier.padding(20.dp)) {
                Text(text = "Measure time of", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                Text(text = taskName.value, fontSize = 20.sp, color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        content = {
            Column(modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .background(Color.White), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
                
                Row(modifier = Modifier
                    .padding(30.dp)
                    .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Mark task as completed", color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    Switch(checked = switchEnabled.value, onCheckedChange = { switchEnabled.value = it }, colors = SwitchDefaults.colors(checkedThumbColor = indigo))
                }

                Text(text = timeText.value, color = Color.Black, fontWeight = FontWeight.Medium, fontSize = 60.sp)

                Button(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                        .height(60.dp),
                    onClick = {
                        if(!timerStarted.value) {
                            timeMeasureViewModel.startTimer()
                            timerStarted.value = true
                        } else {
                            timeMeasureViewModel.pauseTimer()
                            tasksViewModel.updateTaskCompletionTime(taskId, time.value)
                            if(switchEnabled.value) tasksViewModel.changeTaskStatusToCompleted(taskId)
                            navController.navigate(Route.projectDetailsRoute(projectId.value)) {
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
                    Text(if(timerStarted.value) "Stop" else "Start", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
                
            }
        }
    )
}