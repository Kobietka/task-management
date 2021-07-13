package com.kobietka.taskmanagement.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kobietka.taskmanagement.data.ProjectEntity
import com.kobietka.taskmanagement.ui.util.MultiLineTextField
import com.kobietka.taskmanagement.ui.util.NormalTextField
import com.kobietka.taskmanagement.ui.util.Route
import com.kobietka.taskmanagement.viewmodel.ProjectsViewModel


@ExperimentalComposeUiApi
@Composable
fun EditProjectScreen(projectId: Int, projectsViewModel: ProjectsViewModel, navController: NavController){
    val firstTime = remember { mutableStateOf(true) }

    val name = remember { mutableStateOf(TextFieldValue("")) }
    val nameError = remember { mutableStateOf(false) }

    val description = remember { mutableStateOf(TextFieldValue("")) }
    val descriptionError = remember { mutableStateOf(false) }

    val project = remember { mutableStateOf<ProjectEntity?>(null) }

    val focusRequester = remember { FocusRequester() }

    if(firstTime.value){
        projectsViewModel.loadProject(
            id = projectId,
            onFinish = { projectEntity ->
                name.value = TextFieldValue(projectEntity.name)
                description.value = TextFieldValue(projectEntity.description)
                project.value = projectEntity
                firstTime.value = false
            }
        )
    }

    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End){
            Icon(
                modifier = Modifier
                    .padding(20.dp)
                    .height(35.dp)
                    .width(35.dp)
                    .clickable {
                        projectsViewModel.deleteProject(
                            projectId = projectId,
                            onFinish = {
                                navController.navigate(Route.main){
                                    popUpTo(Route.main){
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    },
                imageVector = Icons.Filled.Delete,
                contentDescription = "delete project"
            )
        }
        Row(
            Modifier
                .padding(top = 70.dp, start = 25.dp, end = 25.dp, bottom = 70.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Edit project", fontWeight = FontWeight.Bold, fontSize = 25.sp)
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
                    .padding(top = 50.dp, start = 25.dp, end = 25.dp), horizontalAlignment = Alignment.CenterHorizontally) {

                NormalTextField(
                    state = name,
                    errorState = nameError,
                    enabledState = mutableStateOf(true),
                    fieldName = "Project name",
                    errorMessage = "Please fill this field",
                    focusRequester = focusRequester
                )

                MultiLineTextField(
                    state = description,
                    errorState = descriptionError,
                    enabledState = mutableStateOf(true),
                    fieldName = "Project description",
                    errorMessage = "Please fill this field",
                    lines = 10,
                    focusRequester = focusRequester
                )

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(top = 10.dp),
                    onClick = {
                        if(!nameError.value && !descriptionError.value){
                            projectsViewModel.insertProject(
                                projectEntity = project.value!!.copy(
                                    name = name.value.text,
                                    description = description.value.text
                                ),
                                onFinish = {
                                    navController.navigate(Route.main){
                                        popUpTo(Route.createProject){
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
                    Text(text = "Save", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

            }
        }

    }
}