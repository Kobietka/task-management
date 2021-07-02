package com.kobietka.taskmanagement.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
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
import com.kobietka.taskmanagement.ui.util.NormalTextField
import com.kobietka.taskmanagement.ui.util.Route
import com.kobietka.taskmanagement.viewmodel.ProjectsViewModel


@ExperimentalComposeUiApi
@Composable
fun CreateProjectScreen(projectsViewModel: ProjectsViewModel, navController: NavController){
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
            Text(text = "Create project", fontWeight = FontWeight.Bold, fontSize = 25.sp)
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
                NormalTextField(
                    state = description,
                    errorState = descriptionError,
                    enabledState = mutableStateOf(true),
                    fieldName = "Project description",
                    errorMessage = "Please fill this field",
                    focusRequester = focusRequester
                )

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(top = 10.dp),
                    onClick = { projectsViewModel.insertProject(
                        name = name.value.text,
                        description = description.value.text,
                        onFinish = { navController.navigate(Route.main) }
                    ) },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Yellow
                    ))
                {
                    Text(text = "Create", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

            }
        }

    }

    /*Column(modifier = Modifier.padding(20.dp)) {

        NormalTextField(
            state = name,
            errorState = nameError,
            enabledState = mutableStateOf(true),
            fieldName = "Project name",
            errorMessage = "Please fill this field",
            focusRequester = focusRequester
        )
        NormalTextField(
            state = description,
            errorState = descriptionError,
            enabledState = mutableStateOf(true),
            fieldName = "Project description",
            errorMessage = "Please fill this field",
            focusRequester = focusRequester
        )

        Button(onClick = { }) {

        }
    }*/
}