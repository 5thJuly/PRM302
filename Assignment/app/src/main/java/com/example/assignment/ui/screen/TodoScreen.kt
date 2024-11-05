package com.example.assignment.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.assignment.data.Task
import com.example.assignment.ui.component.EnhancedTaskItem
import com.example.assignment.ui.theme.TaskDeleteButton

@Composable
fun TodoScreen(
    tasks: List<Task>,
    onAddTask: (String) -> Unit,
    onToggleTask: (Int) -> Unit,
    onUpdateTask: (Int, String) -> Unit,
    onDeleteTask: (Int) -> Unit
) {
    var newTaskTitle by remember { mutableStateOf("") }
    var editingTask by remember { mutableStateOf<Task?>(null) }
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(colorScheme.background)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newTaskTitle,
                onValueChange = { newTaskTitle = it },
                modifier = Modifier.weight(1f),
                label = { Text("New Task") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (newTaskTitle.isNotBlank()) {
                        onAddTask(newTaskTitle)
                        newTaskTitle = ""
                    }
                }
            ) {
                Text("Add")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        LazyColumn {
            items(tasks) { task ->
                EnhancedTaskItem(
                    task = task,
                    onToggleComplete = { onToggleTask(task.id) },
                    onDelete = { onDeleteTask(task.id) },
                    onEdit = { editingTask = task }
                )
            }
        }
    }


editingTask?.let { task ->
    var editedTitle by remember { mutableStateOf(task.title) }

    AlertDialog(
        onDismissRequest = { editingTask = null },
        title = { Text("Edit Task", color = colorScheme.onBackground) },
        text = {
            OutlinedTextField(
                value = editedTitle,
                onValueChange = { editedTitle = it },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorScheme.primary,
                    unfocusedBorderColor = colorScheme.onBackground.copy(alpha = 0.5f)
                )
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (editedTitle.isNotBlank()) {
                        onUpdateTask(task.id, editedTitle)
                        editingTask = null
                    }
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = colorScheme.primary
                )
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { editingTask = null },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = TaskDeleteButton
                )
            ) {
                Text("Cancel")
            }
        }
    )
}
}