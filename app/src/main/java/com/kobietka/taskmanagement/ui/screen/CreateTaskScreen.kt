package com.kobietka.taskmanagement.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kobietka.taskmanagement.data.entity.TaskStatusEntity
import com.kobietka.taskmanagement.ui.util.MultiLineTextField
import com.kobietka.taskmanagement.ui.util.NormalTextField
import com.kobietka.taskmanagement.ui.util.Route
import com.kobietka.taskmanagement.viewmodel.CreateTaskViewModel
import com.kobietka.taskmanagement.viewmodel.TasksViewModel


@ExperimentalComposeUiApi
@Composable
fun CreateTaskScreen(
    createTaskViewModel: CreateTaskViewModel,
    tasksViewModel: TasksViewModel,
    navController: NavController,
    onDateClick: () -> Unit
){
    val taskStatuses = createTaskViewModel.taskStatuses().observeAsState(initial = listOf())
    val projectId = createTaskViewModel.projectId().observeAsState(initial = 0)

    val menuExpanded = remember { mutableStateOf(false) }
    val statusBoxText = remember { mutableStateOf("Select task status") }
    val statusId = remember { mutableStateOf(0) }

    val dateText = createTaskViewModel.taskDate().observeAsState(initial = "Due date")

    val name = remember { mutableStateOf(TextFieldValue("")) }
    val nameError = remember { mutableStateOf(false) }
    val description = remember { mutableStateOf(TextFieldValue("")) }
    val descriptionError = remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    Column {
        Row(
            Modifier
                .padding(top = 70.dp, start = 25.dp, end = 25.dp, bottom = 70.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Create task", fontWeight = FontWeight.Bold, fontSize = 25.sp)
        }
        Box(
            Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
                .background(MaterialTheme.colors.primary)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(top = 50.dp, start = 25.dp, end = 25.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {

                NormalTextField(
                    state = name,
                    errorState = nameError,
                    enabledState = mutableStateOf(true),
                    fieldName = "Task name",
                    errorMessage = "Please fill this field",
                    focusRequester = focusRequester
                )

                MultiLineTextField(
                    state = description,
                    errorState = descriptionError,
                    enabledState = mutableStateOf(true),
                    fieldName = "Task description",
                    errorMessage = "Please fill this field",
                    lines = 3,
                    focusRequester = focusRequester
                )

                Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(top = 10.dp)
                        .background(Color.White)
                        .clickable { menuExpanded.value = true },
                    contentAlignment = Alignment.Center) {
                    if(statusBoxText.value != "Select task status"){
                        taskStatuses.value.filter { it.id == statusId.value }.forEach {
                            TaskStatusChip(taskStatus = it)
                        }
                    } else Text(text = statusBoxText.value)
                    DropdownMenu(
                        expanded = menuExpanded.value,
                        onDismissRequest = { menuExpanded.value = false }
                    ) {
                        taskStatuses.value.forEach { taskStatus ->
                            DropdownMenuItem(
                                onClick = {
                                    statusId.value = taskStatus.id!!
                                    statusBoxText.value = taskStatus.name
                                }
                            ) {
                                TaskStatusChip(taskStatus = taskStatus)
                            }
                        }
                    }
                }

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(top = 10.dp)
                    .background(Color.White)
                    .clickable { onDateClick() }, contentAlignment = Alignment.Center){
                    Text(text = dateText.value)
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(top = 10.dp),
                    onClick = {
                          if(name.value.text.isNotBlank() &&
                              description.value.text.isNotBlank() &&
                              statusId.value != 0 && dateText.value != "Due date"){
                              tasksViewModel.insertTask(
                                  projectId = projectId.value,
                                  name = name.value.text,
                                  description = description.value.text,
                                  dueDate = dateText.value,
                                  statusId = statusId.value,
                                  onFinish = {
                                      navController.navigate(Route.projectDetailsRoute(projectId.value)){
                                          popUpTo(Route.projectDetails){
                                              inclusive = true
                                          }
                                      }
                                      createTaskViewModel.clearDate()
                                  }
                              )
                          }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.secondary
                    ))
                {
                    Text(
                        text = "Create",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

            }
        }

    }
}

@Composable
fun TaskStatusChip(taskStatus: TaskStatusEntity){
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .background(
                when (taskStatus.name) {
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
            text = taskStatus.name,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
    }
}