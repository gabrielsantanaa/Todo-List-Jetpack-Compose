package com.gabriel.personal.projects.todolist.ui.addtask

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gabriel.personal.projects.todolist.R
import com.gabriel.personal.projects.todolist.data.db.entity.Task

@Composable
fun AddTaskBody(
    viewModel: AddTaskViewModel,
    onBackPressed: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = null,
                            tint = MaterialTheme.colors.secondary
                        )
                    }
                },
                title = { Text("New task") }
            )
        },
        floatingActionButton = {
            if(viewModel.inputDescription.isNotBlank() && viewModel.inputDescription.isNotBlank()) {
                FloatingActionButton(onClick = {
                    viewModel.saveTask()
                    onBackPressed()
                }) {
                    Icon(Icons.Default.AddTask, null)
                }
            }
        }
    ) {


        Column {
            TaskInput(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                true,
                hint = "Title",
                text = viewModel.inputTitle,
                setText = { viewModel.updateInputTitle(it) }
            )
            Divider()
            TaskInput(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                hint = "Description",
                text = viewModel.inputDescription,
                setText = { viewModel.updateInputDescription(it) }
            )
        }

    }
}

@Composable
fun TaskInput(
    modifier: Modifier = Modifier,
    singleLine: Boolean = false,
    hint: String,
    text: String,
    setText: (String) -> Unit,
) {

    TextField(
        value = text,
        onValueChange = setText,
        modifier = modifier,
        label = { Text(hint) },
        singleLine = singleLine,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
    )
}