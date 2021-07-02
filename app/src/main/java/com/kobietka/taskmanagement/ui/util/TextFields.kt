package com.kobietka.taskmanagement.ui.util


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp


@ExperimentalComposeUiApi
@Composable
fun NormalTextField(state: MutableState<TextFieldValue>, errorState: MutableState<Boolean>, enabledState: MutableState<Boolean>, fieldName: String, errorMessage: String, focusRequester: FocusRequester){
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        value = state.value,
        onValueChange = {
            state.value = it
            errorState.value = state.value.text.isBlank()
        },
        enabled = enabledState.value,
        isError = errorState.value,
        label = {
            if(errorState.value) Text(errorMessage)
            else Text(fieldName)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp)
            .focusRequester(focusRequester)
            .clickable { focusRequester.requestFocus() },
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            disabledTextColor = Color.Black,
            focusedIndicatorColor = Color.Gray,
            focusedLabelColor = Color.Gray,
            cursorColor = Color.Gray,
            errorIndicatorColor = Color.Red,
            errorCursorColor = Color.Red,
            errorLabelColor = Color.Red
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusRequester.requestFocus() },
            onDone = { keyboardController!!.hide() }
        ),
    )
}

@ExperimentalComposeUiApi
@Composable
fun NicknameTextField(state: MutableState<TextFieldValue>, errorState: MutableState<Boolean>, focusRequester: FocusRequester){
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        value = state.value,
        onValueChange = {
            state.value = it
            errorState.value = state.value.text.isBlank()
        },
        isError = errorState.value,
        label = {
            if(errorState.value) Text("Please enter your nickname")
            else Text("Nickname")
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp)
            .focusRequester(focusRequester)
            .clickable { focusRequester.requestFocus() },
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            disabledTextColor = Color.Black,
            focusedIndicatorColor = Color.Gray,
            focusedLabelColor = Color.Gray,
            cursorColor = Color.Gray,
            errorIndicatorColor = Color.Red,
            errorCursorColor = Color.Red,
            errorLabelColor = Color.Red
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusRequester.requestFocus() },
            onDone = { keyboardController!!.hide() }
        ),
    )
}
