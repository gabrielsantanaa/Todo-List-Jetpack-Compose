package com.gabriel.personal.projects.todolist.ui.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable

@Composable
fun AlertDialogTaskDeletion(
    title: String,
    message: String,
    onConfirmPressed: () -> Unit,
    onDismissPressed: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissPressed,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            Button(onClick = onConfirmPressed) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissPressed) {
                Text("Cancel")
            }
        }
    )
}