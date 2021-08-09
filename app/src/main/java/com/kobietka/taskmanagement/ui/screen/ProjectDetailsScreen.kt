package com.kobietka.taskmanagement.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kobietka.taskmanagement.data.entity.StatusEventEntity
import com.kobietka.taskmanagement.data.entity.TaskEntity
import com.kobietka.taskmanagement.data.entity.TaskSessionEntity
import com.kobietka.taskmanagement.data.entity.TaskStatusEntity
import com.kobietka.taskmanagement.ui.util.Route
import com.kobietka.taskmanagement.viewmodel.ProjectsViewModel
import com.kobietka.taskmanagement.viewmodel.StatusChangeViewModel
import com.kobietka.taskmanagement.viewmodel.TasksViewModel
import com.kobietka.taskmanagement.viewmodel.TimeMeasureViewModel


@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun ProjectDetailsScreen(
    projectId: Int,
    projectsViewModel: ProjectsViewModel,
    navController: NavController,
    tasksViewModel: TasksViewModel,
    statusChangeViewModel: StatusChangeViewModel,
    timeMeasureViewModel: TimeMeasureViewModel
){
    val name = remember { mutableStateOf("") }
    val descriptionVisible = remember { mutableStateOf(false) }
    val description = remember { mutableStateOf("") }
    val tasks = remember { mutableStateOf(listOf<TaskEntity>()) }
    val firstTime = remember { mutableStateOf(true) }
    val statuses = remember { mutableStateOf(listOf<TaskStatusEntity>()) }

    val statusChangesVisible = remember { mutableStateOf(false) }

    val isFilterIconVisible = remember { mutableStateOf(true) }

    val infoVisible = remember { mutableStateOf(false) }

    val tasksLabel = remember { mutableStateOf("Tasks") }

    val taskListFilter = remember { mutableStateOf("no filter") }

    val filterMenuExpanded = remember { mutableStateOf(false) }

    val topAppBarVisible = remember { mutableStateOf(true) }

    if(firstTime.value){
        projectsViewModel.loadProjectWithTasks(
            projectId = projectId,
            onFinish = { project, projectTasks ->
                name.value = project.name
                description.value = project.description
                tasks.value = projectTasks
            }
        )
        tasksViewModel.loadTaskStatuses { statusList ->
            statuses.value = statusList
        }
        firstTime.value = !firstTime.value
    }

    Scaffold(
        backgroundColor = MaterialTheme.colors.primary,
        floatingActionButton = {
            if(!statusChangesVisible.value){
                FloatingActionButton(
                    backgroundColor = MaterialTheme.colors.primary,
                    onClick = { navController.navigate(Route.createTaskRoute(projectId)) }) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "add task", tint = Color.Black)
                }
            }
        },
        topBar = {
             ProjectDetailsTopAppBar(
                 topAppBarVisible = topAppBarVisible,
                 name = name,
                 description = description,
                 descriptionVisible = descriptionVisible,
                 statusChangesVisible = statusChangesVisible,
                 infoVisible = infoVisible,
                 tasksLabel = tasksLabel,
                 isFilterIconVisible = isFilterIconVisible
             )
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
                        ProjectDetailsTaskLabel(
                            tasksLabel = tasksLabel,
                            filterMenuExpanded = filterMenuExpanded,
                            isFilterIconVisible = isFilterIconVisible
                        )
                        ProjectDetailsFilterDropdownMenu(
                            filterMenuExpanded = filterMenuExpanded,
                            taskListFilter = taskListFilter,
                            tasksLabel = tasksLabel,
                            statuses = statuses
                        )
                    }
                    if(statusChangesVisible.value){
                        StatusChangeList(
                            projectsViewModel = projectsViewModel,
                            tasks = tasks,
                            statusChangeViewModel = statusChangeViewModel,
                            projectId = projectId,
                            topAppBarState = topAppBarVisible
                        )
                    } else {
                        ProjectDetailsTaskList(
                            taskListFilter = taskListFilter,
                            topAppBarVisible = topAppBarVisible,
                            navController = navController,
                            projectsViewModel = projectsViewModel,
                            timeMeasureViewModel = timeMeasureViewModel,
                            statuses = statuses,
                            tasks = tasks
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun StatusChangeList(
    tasks: MutableState<List<TaskEntity>>,
    projectsViewModel: ProjectsViewModel,
    statusChangeViewModel: StatusChangeViewModel,
    projectId: Int,
    topAppBarState: MutableState<Boolean>
){
    val scrollState = rememberScrollState()
    val eventStatuses = remember { mutableStateOf(listOf<StatusEventEntity>()) }

    topAppBarState.value = !scrollState.isScrollInProgress

    statusChangeViewModel.loadStatusEventsForProject(
        projectId = projectId,
        onFinish = { statusEvents ->
            eventStatuses.value = statusEvents
        }
    )

    if(eventStatuses.value.isEmpty()){
        Row(modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(text = "No status changes", color = Color.Black, fontWeight = FontWeight.Medium, fontSize = 17.sp)
        }
    } else {
        Column(modifier = Modifier
            .padding(top = 5.dp)
            .verticalScroll(scrollState)) {
            eventStatuses.value.reversed().forEach { statusEvent ->
                StatusChangeItem(
                    statusChange = statusEvent,
                    projectsViewModel = projectsViewModel,
                    tasks = tasks
                )
            }
        }
    }

}

@Composable
fun StatusChangeItem(
    statusChange: StatusEventEntity,
    projectsViewModel: ProjectsViewModel,
    tasks: MutableState<List<TaskEntity>>
){
    val oldStatus = remember { mutableStateOf<TaskStatusEntity?>(null) }
    val newStatus = remember { mutableStateOf<TaskStatusEntity?>(null) }
    val task = remember { tasks.value.firstOrNull { it.id == statusChange.taskId } }
    val loadingFinished = remember { mutableStateOf(false) }
    projectsViewModel.loadTaskStatus(
        statusId = statusChange.fromStatus,
        onFinish = { status ->
            oldStatus.value = status
            projectsViewModel.loadTaskStatus(
                statusId = statusChange.toStatus,
                onFinish = { nStatus ->
                    newStatus.value = nStatus
                    loadingFinished.value = true
                }
            )
        }
    )
    if(task != null && loadingFinished.value){
        Card(modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
            .fillMaxWidth(), shape = RoundedCornerShape(10.dp)) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(modifier = Modifier
                    .padding(bottom = 10.dp)
                    .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row {
                        Text(text = task.name, fontWeight = FontWeight.Bold)
                    }
                }
                Row(modifier = Modifier.padding(bottom = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "changed state at ")
                    Text(text = statusChange.date, fontSize = 14.sp, color = Color.Gray)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(modifier = Modifier.padding(end = 10.dp), text = "from")
                    StatusChip(statusName = oldStatus.value!!.name, listOf())
                    Text(modifier = Modifier.padding(start = 10.dp, end = 10.dp), text = "to")
                    StatusChip(statusName = newStatus.value!!.name, listOf())
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun ProjectDetailsTopAppBar(
    topAppBarVisible: MutableState<Boolean>,
    name: MutableState<String>,
    description: MutableState<String>,
    descriptionVisible: MutableState<Boolean>,
    statusChangesVisible: MutableState<Boolean>,
    infoVisible: MutableState<Boolean>,
    tasksLabel: MutableState<String>,
    isFilterIconVisible: MutableState<Boolean>
){
    AnimatedVisibility(visible = topAppBarVisible.value) {
        Column(horizontalAlignment = Alignment.CenterHorizontally){
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                Column {
                    Text(text = "Welcome to", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                    AnimatedVisibility(visible = name.value != "") {
                        Text(modifier = Modifier.clickable { descriptionVisible.value = !descriptionVisible.value }, text = name.value, fontSize = 20.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                    AnimatedVisibility(visible = descriptionVisible.value) {
                        Text(modifier = Modifier.padding(top = 5.dp), text = description.value, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                    }
                }
                Row {
                    if(!statusChangesVisible.value){
                        Icon(modifier = Modifier
                            .padding(end = 20.dp)
                            .size(30.dp, 30.dp)
                            .clickable {
                                statusChangesVisible.value = !statusChangesVisible.value
                                tasksLabel.value = "Status history"
                                isFilterIconVisible.value = !isFilterIconVisible.value
                                if (isFilterIconVisible.value) tasksLabel.value = "Tasks"
                            },
                            imageVector = Icons.Outlined.History,
                            contentDescription = "status history",
                            tint = Color.Black
                        )
                    } else {
                        Icon(modifier = Modifier
                            .padding(end = 20.dp)
                            .size(30.dp, 30.dp)
                            .clickable {
                                statusChangesVisible.value = !statusChangesVisible.value
                                tasksLabel.value = "Status history"
                                isFilterIconVisible.value = !isFilterIconVisible.value
                                if (isFilterIconVisible.value) tasksLabel.value = "Tasks"
                            },
                            imageVector = Icons.Outlined.List,
                            contentDescription = "tasks",
                            tint = Color.Black
                        )
                    }
                    Icon(modifier = Modifier
                        .size(30.dp, 30.dp)
                        .clickable { infoVisible.value = !infoVisible.value },
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "info",
                        tint = Color.Black
                    )
                }
            }
            AnimatedVisibility(visible = infoVisible.value) {
                Card(modifier = Modifier.padding(10.dp), backgroundColor = Color.White) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(text = "To create task click + button.")
                        Text(text = "Click on task to edit it.")
                        Text(text = "Long click on task to measure time.")
                    }
                }
            }
        }
    }
}

@Composable
fun ProjectDetailsFilterDropdownMenu(
    filterMenuExpanded: MutableState<Boolean>,
    taskListFilter: MutableState<String>,
    tasksLabel: MutableState<String>,
    statuses: MutableState<List<TaskStatusEntity>>
){
    DropdownMenu(expanded = filterMenuExpanded.value, onDismissRequest = { filterMenuExpanded.value = false }, offset = DpOffset(270.dp, 0.dp)) {
        DropdownMenuItem(onClick = { taskListFilter.value = "no filter"; filterMenuExpanded.value = false; tasksLabel.value = "No filter" }) {
            Text(text = "No filter")
        }
        DropdownMenuItem(onClick = { taskListFilter.value = "by status"; filterMenuExpanded.value = false; tasksLabel.value = "By status" }) {
            Text(text = "By status")
        }
        DropdownMenuItem(onClick = { taskListFilter.value = "archived"; filterMenuExpanded.value = false; tasksLabel.value = "Archived" }) {
            Text(text = "Archived")
        }
        statuses.value.forEach { taskStatus ->
            DropdownMenuItem(onClick = { taskListFilter.value = taskStatus.name; filterMenuExpanded.value = false; tasksLabel.value = taskStatus.name.firstCapital() }) {
                Text(text = taskStatus.name.firstCapital())
            }
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun ProjectDetailsTaskList(
    taskListFilter: MutableState<String>,
    topAppBarVisible: MutableState<Boolean>,
    navController: NavController,
    projectsViewModel: ProjectsViewModel,
    timeMeasureViewModel: TimeMeasureViewModel,
    statuses: MutableState<List<TaskStatusEntity>>,
    tasks: MutableState<List<TaskEntity>>
){
    when(taskListFilter.value){
        "no filter" -> {
            TaskList(
                topAppBarState = topAppBarVisible,
                tasks = tasks.value.filter { !it.isArchived },
                navController = navController,
                projectsViewModel = projectsViewModel,
                timeMeasureViewModel = timeMeasureViewModel
            )
        }
        "by status" -> {
            TaskListByStatus(
                topAppBarState = topAppBarVisible,
                statuses = statuses.value,
                tasks = tasks.value.filter { !it.isArchived },
                navController = navController,
                projectsViewModel = projectsViewModel,
                timeMeasureViewModel = timeMeasureViewModel
            )
        }
        "archived" -> {
            TaskList(
                topAppBarState = topAppBarVisible,
                tasks = tasks.value.filter { it.isArchived },
                navController = navController,
                projectsViewModel = projectsViewModel,
                timeMeasureViewModel = timeMeasureViewModel
            )
        }
        else -> {
            statuses.value.forEach { taskStatus ->
                if(taskStatus.name == taskListFilter.value){
                    TaskListWithOnlyOneStatus(
                        topAppBarState = topAppBarVisible,
                        statusId = taskStatus.id!!,
                        tasks = tasks.value.filter { !it.isArchived },
                        navController = navController,
                        projectsViewModel = projectsViewModel,
                        timeMeasureViewModel = timeMeasureViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun ProjectDetailsTaskLabel(
    tasksLabel: MutableState<String>,
    filterMenuExpanded: MutableState<Boolean>,
    isFilterIconVisible: MutableState<Boolean>
){
    Row(modifier = Modifier.padding(top = 32.dp, start = 20.dp, end = 20.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = tasksLabel.value, fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        if(isFilterIconVisible.value){
            Icon(
                modifier = Modifier
                    .size(30.dp, 30.dp)
                    .clickable {
                        filterMenuExpanded.value = !filterMenuExpanded.value
                    },
                imageVector = Icons.Filled.FilterAlt,
                contentDescription = "filter",
                tint = Color.Black
            )
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun Task(
    taskEntity: TaskEntity,
    projectsViewModel: ProjectsViewModel,
    navController: NavController,
    timeMeasureViewModel: TimeMeasureViewModel
){
    val status = remember { mutableStateOf("") }
    val sessions = remember { mutableStateOf<List<TaskSessionEntity>>(listOf()) }
    val firstTime = remember { mutableStateOf(true) }

    if(firstTime.value){
        projectsViewModel.loadTaskStatus(
            statusId = taskEntity.statusId,
            onFinish = { taskStatusEntity ->
                status.value = taskStatusEntity.name
                timeMeasureViewModel.loadSessions(
                    taskId = taskEntity.id!!,
                    onFinish = { taskSessions ->
                        sessions.value = taskSessions
                    }
                )
            }
        )
    }

    Card(shape = RoundedCornerShape(10.dp), modifier = Modifier
        .padding(bottom = 10.dp)
        .combinedClickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(),
            onClick = { navController.navigate(Route.taskDetailsRoute(taskEntity.id!!)) },
            onLongClick = { navController.navigate(Route.measureTimeRoute(taskEntity.id!!)) }
        )) {
        Column(modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text(modifier = Modifier.padding(bottom = 5.dp), text = taskEntity.name, fontSize = 18.sp, fontWeight = FontWeight.Bold, softWrap = true, overflow = TextOverflow.Ellipsis, maxLines = 2)
            }
            Text(modifier = Modifier.padding(bottom = 5.dp), text = taskEntity.dueDate, fontSize = 16.sp, fontWeight = FontWeight.Normal, color = Color.Gray)
            AnimatedVisibility(visible = status.value != "") {
                StatusChip(statusName = status.value, sessions.value)
            }
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun TaskList(
    topAppBarState: MutableState<Boolean>,
    tasks: List<TaskEntity>,
    navController: NavController,
    projectsViewModel: ProjectsViewModel,
    timeMeasureViewModel: TimeMeasureViewModel
){
    val lazyListState = rememberLazyListState()

    topAppBarState.value = !lazyListState.isScrollInProgress

    if(tasks.isEmpty()){
        Row(modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(text = "No tasks", color = Color.Black, fontWeight = FontWeight.Medium, fontSize = 17.sp)
        }
    } else {
        LazyColumn(modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 5.dp)
            .fillMaxSize(), state = lazyListState) {
            items(tasks.size){
                Task(
                    taskEntity = tasks[it],
                    navController = navController,
                    projectsViewModel = projectsViewModel,
                    timeMeasureViewModel = timeMeasureViewModel
                )
            }
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun TaskListByStatus(
    topAppBarState: MutableState<Boolean>,
    statuses: List<TaskStatusEntity>,
    tasks: List<TaskEntity>,
    navController: NavController,
    projectsViewModel: ProjectsViewModel,
    timeMeasureViewModel: TimeMeasureViewModel
){
    val scrollState = rememberScrollState()

    topAppBarState.value = !scrollState.isScrollInProgress

    if(tasks.isEmpty()){
        Row(modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(text = "No tasks", color = Color.Black, fontWeight = FontWeight.Medium, fontSize = 17.sp)
        }
    } else {
        Column(
            Modifier
                .verticalScroll(scrollState, true)
                .padding(start = 20.dp, end = 20.dp, top = 5.dp)) {
            statuses.forEach { status ->
                val tasksWithStatus = tasks.filter { task -> task.statusId == status.id }
                if(tasksWithStatus.isNotEmpty())  Text(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp), text = status.name, color = Color.Black, fontWeight = FontWeight.Medium, fontSize = 17.sp)
                tasksWithStatus.forEach {
                    Task(
                        taskEntity = it,
                        navController = navController,
                        projectsViewModel = projectsViewModel,
                        timeMeasureViewModel = timeMeasureViewModel
                    )
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun TaskListWithOnlyOneStatus(
    topAppBarState: MutableState<Boolean>,
    statusId: Int,
    tasks: List<TaskEntity>,
    navController: NavController,
    projectsViewModel: ProjectsViewModel,
    timeMeasureViewModel: TimeMeasureViewModel
){
    val filteredTasks = tasks.filter { task -> task.statusId == statusId }
    val lazyListState = rememberLazyListState()

    topAppBarState.value = !lazyListState.isScrollInProgress

    if(filteredTasks.isEmpty()){
        Row(modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(text = "No tasks", color = Color.Black, fontWeight = FontWeight.Medium, fontSize = 17.sp)
        }
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 5.dp)
                .fillMaxSize(), state = lazyListState) {
                items(filteredTasks.size){
                    Task(
                        taskEntity = filteredTasks[it],
                        navController = navController,
                        projectsViewModel = projectsViewModel,
                        timeMeasureViewModel = timeMeasureViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun StatusChip(statusName: String, taskSessions: List<TaskSessionEntity>){
    val seconds = taskSessions.fold(0) { acc, taskSessionEntity -> acc + taskSessionEntity.timeInSeconds }
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .background(
                when (statusName) {
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
            text = statusName + if(seconds >= 60) " ${seconds/60} minutes" else "",
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
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

fun String.firstCapital(): String {
    return this.first().uppercase() + this.substring(1)
}















