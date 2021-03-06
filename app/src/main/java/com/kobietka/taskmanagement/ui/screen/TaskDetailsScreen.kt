package com.kobietka.taskmanagement.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Unarchive
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
import com.kobietka.taskmanagement.ui.util.MultiLineTextField
import com.kobietka.taskmanagement.ui.util.NormalTextField
import com.kobietka.taskmanagement.ui.util.Route
import com.kobietka.taskmanagement.viewmodel.TaskDetailsViewModel
import com.kobietka.taskmanagement.viewmodel.TasksViewModel


@ExperimentalComposeUiApi
@Composable
fun TaskDetailsScreen(
    taskDetailsViewModel: TaskDetailsViewModel,
    tasksViewModel: TasksViewModel,
    navController: NavController,
    onDateClick: () -> Unit
){
    val taskStatuses = taskDetailsViewModel.taskStatuses().observeAsState(initial = listOf())
    val taskStatusId = taskDetailsViewModel.taskStatusId().observeAsState(initial = 0)
    val taskName = taskDetailsViewModel.taskName().observeAsState(initial = "")
    val taskDescription = taskDetailsViewModel.taskDescription().observeAsState(initial = "")
    val taskDueDate = taskDetailsViewModel.taskDueDate().observeAsState(initial = "Due date")
    val taskId = taskDetailsViewModel.taskId().observeAsState(initial = 0)
    val projectId = taskDetailsViewModel.projectId().observeAsState(initial = 0)
    val loadingFinished = taskDetailsViewModel.loadingFinished().observeAsState(initial = false)
    val isTaskArchived = taskDetailsViewModel.isTaskArchived().observeAsState(initial = false)

    val firstTime = remember { mutableStateOf(true) }
    val menuExpanded = remember { mutableStateOf(false) }
    val statusBoxText = remember { mutableStateOf("Select task status") }
    val statusId = remember { mutableStateOf(-1) }

    val dateText = remember { mutableStateOf("Due date") }
    val dateTextFromViewModel = taskDetailsViewModel.taskDate().observeAsState(initial = dateText.value)
    val dateClicked = remember { mutableStateOf(false) }
    val dateClickedFirstTime = remember { mutableStateOf(false) }

    val name = remember { mutableStateOf(TextFieldValue("")) }
    val description = remember { mutableStateOf(TextFieldValue("")) }

    val nameError = remember { mutableStateOf(false) }
    val descriptionError = remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    if(dateClicked.value){
        if(dateClickedFirstTime.value){
            taskDetailsViewModel.clearDate()
            dateClickedFirstTime.value = false
        }
        dateText.value = dateTextFromViewModel.value
    }

    if(loadingFinished.value && firstTime.value){
        name.value = TextFieldValue(taskName.value)
        description.value = TextFieldValue(taskDescription.value)
        statusId.value = taskStatusId.value
        dateText.value = taskDueDate.value
        taskStatuses.value.first { it.id == taskStatusId.value }.also { status ->
            statusId.value = status.id!!
            statusBoxText.value = status.name
        }
        firstTime.value = false
    }

    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End){
            if(isTaskArchived.value){
                Icon(
                    modifier = Modifier
                        .padding(end = 10.dp, top = 20.dp, bottom = 20.dp)
                        .height(35.dp)
                        .width(35.dp)
                        .clickable {
                            tasksViewModel.unarchiveTask(
                                taskId = taskId.value,
                                onFinish = {
                                    navController.navigate(Route.projectDetailsRoute(projectId.value)) {
                                        popUpTo(Route.projectDetails) {
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        },
                    imageVector = Icons.Filled.Unarchive,
                    contentDescription = "unarchive task"
                )
            } else {
                Icon(
                    modifier = Modifier
                        .padding(end = 10.dp, top = 20.dp, bottom = 20.dp)
                        .height(35.dp)
                        .width(35.dp)
                        .clickable {
                            tasksViewModel.archiveTask(
                                taskId = taskId.value,
                                onFinish = {
                                    navController.navigate(Route.projectDetailsRoute(projectId.value)) {
                                        popUpTo(Route.projectDetails) {
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        },
                    imageVector = Icons.Filled.Archive,
                    contentDescription = "archive task"
                )
            }
            Icon(
                modifier = Modifier
                    .padding(20.dp)
                    .height(35.dp)
                    .width(35.dp)
                    .clickable {
                        tasksViewModel.deleteTask(
                            taskId = taskId.value,
                            onFinish = {
                                navController.navigate(Route.projectDetailsRoute(projectId.value)) {
                                    popUpTo(Route.projectDetails) {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    },
                imageVector = Icons.Filled.Delete,
                contentDescription = "delete task"
            )
        }
        Row(
            Modifier
                .padding(top = 70.dp, start = 25.dp, end = 25.dp, bottom = 70.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Edit task",
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )
        }
        Box(
            Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
                .background(MaterialTheme.colors.primary)
        ) {
            Column(
                modifier = Modifier
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
                    lines = 10,
                    focusRequester = focusRequester
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(top = 10.dp)
                        .background(Color.White)
                        .clickable { menuExpanded.value = true },
                    contentAlignment = Alignment.Center) {
                    if(statusBoxText.value != "Select task status"){
                        taskStatuses.value.first { it.id == statusId.value }.also {
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
                    .clickable {
                        onDateClick()
                        dateClicked.value = true
                        dateClickedFirstTime.value = true
                    }, contentAlignment = Alignment.Center){
                    Text(text = if(dateClicked.value) dateTextFromViewModel.value else dateText.value)
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(top = 10.dp),
                    onClick = {
                        if(name.value.text.isNotBlank() &&
                            description.value.text.isNotBlank() &&
                            statusId.value != -1 &&
                            dateText.value != "Due date"
                        ){
                            tasksViewModel.updateTask(
                                taskId = taskId.value,
                                name = name.value.text,
                                description = description.value.text,
                                newStatusId = statusId.value,
                                dueDate = dateText.value,
                                onFinish = {
                                    navController.navigate(Route.projectDetailsRoute(projectId.value)){
                                        popUpTo(Route.projectDetails){
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.secondary
                    ))
                {
                    Text(
                        text = "Save",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

            }
        }

    }
}