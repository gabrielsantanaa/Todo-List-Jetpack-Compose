package com.gabriel.personal.projects.todolist.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gabriel.personal.projects.todolist.data.db.entity.Task

@Composable
fun TaskItemView(
    task: Task,
    modifier: Modifier = Modifier,
    onCheckboxPressed: (Boolean) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val isCompleted by mutableStateOf(task.isComplete)
        Checkbox(checked = isCompleted, onCheckedChange = { onCheckboxPressed(!isCompleted) })
        Text(
            modifier = Modifier
                .padding(start = 8.dp),
            text = "${task.taskTitle} #${task.taskId}",
            style = MaterialTheme.typography.h6
        )
    }
}