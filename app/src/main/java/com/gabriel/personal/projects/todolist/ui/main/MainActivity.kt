package com.gabriel.personal.projects.todolist.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.gabriel.personal.projects.todolist.ui.addtask.AddTaskBody
import com.gabriel.personal.projects.todolist.ui.addtask.AddTaskViewModel
import com.gabriel.personal.projects.todolist.ui.theme.ComposeCodeLabTheme
import com.gabriel.personal.projects.todolist.ui.todolist.TodoListViewModel
import com.gabriel.personal.projects.todolist.ui.todolist.TodoBody
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val toDoListViewModel by viewModels<TodoListViewModel>()
    private val addTaskViewModel by viewModels<AddTaskViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                MyScreenContent(toDoListViewModel, addTaskViewModel)
            }
        }

    }

}

@Composable
fun MyScreenContent(todoListViewModel: TodoListViewModel, addTaskViewModel: AddTaskViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "todo") {
        composable("todo") {
            TodoBody(
                todoListViewModel,
                onAddTaskPressed = {
                    navController.navigate("addTask")
                },
            )
        }
        composable("addTask") {
            AddTaskBody(
                addTaskViewModel,
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    ComposeCodeLabTheme {
        Surface(color = MaterialTheme.colors.background) {
            content()
        }
    }
}

