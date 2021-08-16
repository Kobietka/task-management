package com.kobietka.taskmanagement.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.NavigateNext
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kobietka.taskmanagement.data.entity.StatusEventEntity
import com.kobietka.taskmanagement.data.entity.TaskEntity
import com.kobietka.taskmanagement.data.entity.TaskSessionEntity
import com.kobietka.taskmanagement.data.entity.TaskStatusEntity
import com.kobietka.taskmanagement.ui.theme.indigo
import com.kobietka.taskmanagement.ui.theme.orange
import com.kobietka.taskmanagement.ui.util.Route
import com.kobietka.taskmanagement.viewmodel.ProjectDetailsViewModel
import com.kobietka.taskmanagement.viewmodel.StatusChangeViewModel
import com.kobietka.taskmanagement.viewmodel.TasksViewModel
import kotlinx.coroutines.delay

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun ProjectDetailsScreen(
    tasksViewModel: TasksViewModel,
    projectDetailsViewModel: ProjectDetailsViewModel,
    statusChangeViewModel: StatusChangeViewModel,
    navController: NavController,
    isMeasuringTimeBlocked: Boolean
){
    val projectName = projectDetailsViewModel.projectName().observeAsState(initial = "")
    val projectDescription = projectDetailsViewModel.projectDescription().observeAsState(initial = "")
    val projectDescriptionVisible = remember { mutableStateOf(false) }
    val projectId = projectDetailsViewModel.projectId().observeAsState(initial = 0)
    val projectTasks = projectDetailsViewModel.projectFilteredTasks().observeAsState(initial = listOf())
    val tasksStatuses = tasksViewModel.taskStatuses().observeAsState(initial = listOf())
    val statusEvents = statusChangeViewModel.statusChanges().observeAsState(initial = listOf())
    val taskSessions = projectDetailsViewModel.taskSessions().observeAsState(initial = listOf())

    val statusChangesVisible = remember { mutableStateOf(false) }
    val isFilterIconVisible = remember { mutableStateOf(true) }
    val infoVisible = remember { mutableStateOf(false) }
    val tasksLabel = remember { mutableStateOf("Tasks") }
    val taskListFilter = remember { mutableStateOf("no filter") }
    val filterMenuExpanded = remember { mutableStateOf(false) }
    val topAppBarVisible = remember { mutableStateOf(true) }
    val isTaskFiltering = remember { mutableStateOf(false) }

    val loadingFinished = projectDetailsViewModel.loadingFinished().observeAsState(initial = false)
    val isUiVisible = remember { mutableStateOf(false) }

    val labelHeight: Dp by animateDpAsState(targetValue = if (!isTaskFiltering.value) 90.dp else 120.dp)

    LaunchedEffect(key1 = 1, block = {
        while (!loadingFinished.value){
            delay(400)
        }
        isUiVisible.value = true
    })

    if(!isUiVisible.value){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(orange),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    } else {
        Scaffold(
            backgroundColor = MaterialTheme.colors.primary,
            floatingActionButton = {
                if(!statusChangesVisible.value){
                    FloatingActionButton(
                        backgroundColor = MaterialTheme.colors.primary,
                        onClick = { navController.navigate(Route.createTaskRoute(projectId.value)) }) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "add task",
                            tint = Color.Black
                        )
                    }
                }
            },
            topBar = {
                ProjectDetailsTopAppBar(
                    topAppBarVisible = topAppBarVisible,
                    name = projectName,
                    description = projectDescription,
                    descriptionVisible = projectDescriptionVisible,
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
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(labelHeight),
                            backgroundColor = MaterialTheme.colors.secondary,
                            elevation = 20.dp) {
                            ProjectDetailsTaskLabel(
                                tasksLabel = tasksLabel,
                                filterMenuExpanded = filterMenuExpanded,
                                isFilterIconVisible = isFilterIconVisible,
                                isTaskFiltering = isTaskFiltering,
                                onFilterValueChanged = { value ->
                                    projectDetailsViewModel.filterTasks(value)
                                },
                                onFilterCancel = {
                                    projectDetailsViewModel.filterTasks("")
                                }
                            )
                            ProjectDetailsFilterDropdownMenu(
                                filterMenuExpanded = filterMenuExpanded,
                                taskListFilter = taskListFilter,
                                tasksLabel = tasksLabel,
                                statuses = tasksStatuses
                            )
                        }
                        if(statusChangesVisible.value){
                            StatusChangeList(
                                tasks = projectTasks,
                                topAppBarState = topAppBarVisible,
                                taskStatuses = tasksStatuses,
                                statusChanges = statusEvents
                            )
                        } else {
                            ProjectDetailsTaskList(
                                taskListFilter = taskListFilter,
                                topAppBarVisible = topAppBarVisible,
                                navController = navController,
                                statuses = tasksStatuses,
                                tasks = projectTasks,
                                taskSessions = taskSessions,
                                isMeasuringTimeBlocked = isMeasuringTimeBlocked
                            )
                        }
                    }
                }
            }
        )
    }
}

@ExperimentalAnimationApi
@Composable
fun ProjectDetailsTopAppBar(
    topAppBarVisible: MutableState<Boolean>,
    name: State<String>,
    description: State<String>,
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
                .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically){
                Column {
                    Text(
                        text = "Welcome to",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    AnimatedVisibility(visible = name.value != "") {
                        Text(
                            modifier = Modifier.clickable {
                                descriptionVisible.value = !descriptionVisible.value
                            },
                            text = name.value,
                            fontSize = 20.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    AnimatedVisibility(visible = descriptionVisible.value) {
                        Text(
                            modifier = Modifier.padding(top = 5.dp),
                            text = description.value,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
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
    statuses: State<List<TaskStatusEntity>>
){
    DropdownMenu(
        expanded = filterMenuExpanded.value,
        onDismissRequest = { filterMenuExpanded.value = false },
        offset = DpOffset(270.dp, 0.dp)) {
        DropdownMenuItem(
            onClick = {
                taskListFilter.value = "no filter"
                filterMenuExpanded.value = false
                tasksLabel.value = "No filter"
            }
        ) {
            Text(text = "No filter")
        }
        DropdownMenuItem(
            onClick = {
                taskListFilter.value = "by status"
                filterMenuExpanded.value = false
                tasksLabel.value = "By status"
            }
        ) {
            Text(text = "By status")
        }
        DropdownMenuItem(
            onClick = {
                taskListFilter.value = "archived"
                filterMenuExpanded.value = false
                tasksLabel.value = "Archived"
            }
        ) {
            Text(text = "Archived")
        }
        statuses.value.forEach { taskStatus ->
            DropdownMenuItem(
                onClick = {
                    taskListFilter.value = taskStatus.name
                    filterMenuExpanded.value = false
                    tasksLabel.value = taskStatus.name.firstCapital()
                }
            ) {
                Text(text = taskStatus.name.firstCapital())
            }
        }
    }
}

@Composable
fun StatusChangeList(
    tasks: State<List<TaskEntity>>,
    topAppBarState: MutableState<Boolean>,
    statusChanges: State<List<StatusEventEntity>>,
    taskStatuses: State<List<TaskStatusEntity>>
){
    val scrollState = rememberScrollState()

    topAppBarState.value = !scrollState.isScrollInProgress

    if(statusChanges.value.isEmpty()){
        Row(modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                text = "No status changes",
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 17.sp
            )
        }
    } else {
        Column(modifier = Modifier
            .padding(top = 5.dp)
            .verticalScroll(scrollState)) {
            statusChanges.value.reversed().forEach { statusEvent ->
                StatusChangeItem(
                    statusChange = statusEvent,
                    tasks = tasks,
                    taskStatuses = taskStatuses
                )
            }
        }
    }

}

@Composable
fun StatusChangeItem(
    statusChange: StatusEventEntity,
    taskStatuses: State<List<TaskStatusEntity>>,
    tasks: State<List<TaskEntity>>
){
    val oldStatus = remember {
        taskStatuses.value.first { status -> status.id == statusChange.fromStatus }
    }
    val newStatus = remember {
        taskStatuses.value.first { status -> status.id == statusChange.toStatus }
    }
    val task = remember { tasks.value.firstOrNull { it.id == statusChange.taskId } }

    if(task != null){
        Card(modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
            .fillMaxWidth(), shape = RoundedCornerShape(10.dp)) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                    Row {
                        Text(text = task.name, fontWeight = FontWeight.Bold)
                    }
                }
                Row(
                    modifier = Modifier.padding(bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "changed state at ")
                    Text(text = statusChange.date, fontSize = 14.sp, color = Color.Gray)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    StatusChip(statusName = oldStatus.name, listOf())
                    Icon(modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp)
                        .size(30.dp, 30.dp),
                        imageVector = Icons.Outlined.NavigateNext,
                        contentDescription = "to state",
                        tint = Color.Black
                    )
                    StatusChip(statusName = newStatus.name, listOf())
                }
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
    statuses: State<List<TaskStatusEntity>>,
    tasks: State<List<TaskEntity>>,
    taskSessions: State<List<TaskSessionEntity>>,
    isMeasuringTimeBlocked: Boolean
){
    when(taskListFilter.value){
        "no filter" -> {
            TaskList(
                topAppBarState = topAppBarVisible,
                tasks = tasks.value.filter { !it.isArchived },
                navController = navController,
                statuses = statuses,
                taskSessions = taskSessions,
                isMeasuringTimeBlocked = isMeasuringTimeBlocked
            )
        }
        "by status" -> {
            TaskListByStatus(
                topAppBarState = topAppBarVisible,
                statuses = statuses,
                tasks = tasks.value.filter { !it.isArchived },
                navController = navController,
                taskSessions = taskSessions,
                isMeasuringTimeBlocked = isMeasuringTimeBlocked
            )
        }
        "archived" -> {
            TaskList(
                topAppBarState = topAppBarVisible,
                tasks = tasks.value.filter { it.isArchived },
                navController = navController,
                statuses = statuses,
                taskSessions = taskSessions,
                isMeasuringTimeBlocked = isMeasuringTimeBlocked
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
                        statuses = statuses,
                        taskSessions = taskSessions,
                        isMeasuringTimeBlocked = isMeasuringTimeBlocked
                    )
                }
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
    statuses: State<List<TaskStatusEntity>>,
    taskSessions: State<List<TaskSessionEntity>>,
    isMeasuringTimeBlocked: Boolean
){
    val lazyListState = rememberLazyListState()

    topAppBarState.value = !lazyListState.isScrollInProgress

    if(tasks.isEmpty()){
        Row(modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                text = "No tasks",
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 17.sp
            )
        }
    } else {
        LazyColumn(modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 5.dp)
            .fillMaxSize(), state = lazyListState) {
            items(tasks.size){
                Task(
                    taskEntity = tasks[it],
                    navController = navController,
                    statuses = statuses.value,
                    taskSessions = taskSessions.value.filter { session -> session.taskId == tasks[it].id },
                    isMeasuringTimeBlocked = isMeasuringTimeBlocked
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
    statuses: State<List<TaskStatusEntity>>,
    tasks: List<TaskEntity>,
    navController: NavController,
    taskSessions: State<List<TaskSessionEntity>>,
    isMeasuringTimeBlocked: Boolean
){
    val scrollState = rememberScrollState()
    topAppBarState.value = !scrollState.isScrollInProgress

    if(tasks.isEmpty()){
        Row(modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                text = "No tasks",
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 17.sp
            )
        }
    } else {
        Column(
            Modifier
                .verticalScroll(scrollState, true)
                .padding(start = 20.dp, end = 20.dp, top = 5.dp)) {
            statuses.value.forEach { status ->
                val tasksWithStatus = tasks.filter { task -> task.statusId == status.id }
                if(tasksWithStatus.isNotEmpty())
                    Text(
                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                        text = status.name,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium,
                        fontSize = 17.sp
                    )
                tasksWithStatus.forEach {
                    Task(
                        taskEntity = it,
                        navController = navController,
                        statuses = statuses.value,
                        taskSessions = taskSessions.value.filter { session -> session.taskId == it.id },
                        isMeasuringTimeBlocked = isMeasuringTimeBlocked
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
    isFilterIconVisible: MutableState<Boolean>,
    isTaskFiltering: MutableState<Boolean>,
    onFilterValueChanged: (String) -> Unit,
    onFilterCancel: () -> Unit
){
    val filterValue = remember { mutableStateOf("") }

    if(isTaskFiltering.value){
        Row(
            modifier = Modifier.padding(top = 32.dp, start = 20.dp, end = 20.dp, bottom = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(end = 10.dp),
                value = filterValue.value,
                onValueChange = { new ->
                    onFilterValueChanged(new.trim())
                    filterValue.value = new
                },
                label = {
                    Text(text = "Search")
                },
                colors = TextFieldDefaults.textFieldColors(
                    focusedLabelColor = indigo
                )
            )
            Icon(
                modifier = Modifier
                    .size(30.dp, 30.dp)
                    .clickable {
                        isTaskFiltering.value = false
                        filterValue.value = ""
                        onFilterCancel()
                    },
                imageVector = Icons.Filled.Cancel,
                contentDescription = "cancel",
                tint = Color.Black
            )
        }
    } else {
        Row(
            modifier = Modifier.padding(top = 32.dp, start = 20.dp, end = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = tasksLabel.value,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            if(isFilterIconVisible.value){
                Row {
                    Icon(
                        modifier = Modifier
                            .padding(end = 20.dp)
                            .size(30.dp, 30.dp)
                            .clickable {
                                isTaskFiltering.value = true
                            },
                        imageVector = Icons.Filled.Search,
                        contentDescription = "search",
                        tint = Color.Black
                    )
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
    statuses: State<List<TaskStatusEntity>>,
    taskSessions: State<List<TaskSessionEntity>>,
    isMeasuringTimeBlocked: Boolean
){
    val filteredTasks = tasks.filter { task -> task.statusId == statusId }
    val lazyListState = rememberLazyListState()

    topAppBarState.value = !lazyListState.isScrollInProgress

    if(filteredTasks.isEmpty()){
        Row(modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                text = "No tasks",
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 17.sp
            )
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
                        statuses = statuses.value,
                        taskSessions = taskSessions.value.filter { session -> session.taskId == tasks[it].id },
                        isMeasuringTimeBlocked = isMeasuringTimeBlocked
                    )
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun Task(
    taskEntity: TaskEntity,
    navController: NavController,
    statuses: List<TaskStatusEntity>,
    taskSessions: List<TaskSessionEntity>,
    isMeasuringTimeBlocked: Boolean
){
    val status = statuses.first { status -> taskEntity.statusId == status.id }

    Card(shape = RoundedCornerShape(10.dp), modifier = Modifier
        .padding(bottom = 10.dp)
        .combinedClickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(),
            onClick = { navController.navigate(Route.taskDetailsRoute(taskEntity.id!!)) },
            onLongClick = { if(!isMeasuringTimeBlocked) navController.navigate(Route.measureTimeRoute(taskEntity.id!!)) }
        )) {
        Column(modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween){
                Text(
                    modifier = Modifier.padding(bottom = 5.dp),
                    text = taskEntity.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    softWrap = true,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
            }
            Text(
                modifier = Modifier.padding(bottom = 5.dp),
                text = taskEntity.dueDate,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray
            )
            StatusChip(statusName = status.name, taskSessions = taskSessions)
        }
    }
}

@Composable
fun StatusChip(statusName: String, taskSessions: List<TaskSessionEntity>){
    val seconds = taskSessions.fold(0) { acc, taskSessionEntity ->
        acc + taskSessionEntity.timeInSeconds
    }
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
            text = if(statusName == "Completed") {
                statusName + if(seconds >= 60) " in ${seconds/60} minutes" else ""
            } else statusName,
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
