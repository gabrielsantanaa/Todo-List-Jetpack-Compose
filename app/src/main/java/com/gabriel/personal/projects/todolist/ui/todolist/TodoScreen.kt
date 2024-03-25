package com.gabriel.personal.projects.todolist.ui.todolist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.gabriel.personal.projects.todolist.R
import com.gabriel.personal.projects.todolist.enums.TaskFilterType
import com.gabriel.personal.projects.todolist.ui.components.AlertDialogTaskDeletion
import com.gabriel.personal.projects.todolist.ui.components.SearchBar
import com.gabriel.personal.projects.todolist.ui.components.TaskItemView
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TodoBody(
    viewModel: TodoListViewModel,
    onAddTaskPressed: () -> Unit,
) {
    val tasks = viewModel.tasks.collectAsLazyPagingItems()
    val appBarHeightInDP = 56.dp
    val appBarHeightInPX = with(LocalDensity.current) { appBarHeightInDP.roundToPx().toFloat() }

    val appBarOffsetHeightPx = remember { mutableStateOf(0f) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {

                val delta = available.y
                val newOffset = appBarOffsetHeightPx.value + delta
                appBarOffsetHeightPx.value = newOffset.coerceIn(-appBarHeightInPX, 0f)
                return Offset.Zero
            }
        }
    }


    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {

            val scrollState = rememberScrollState()
            Text(
                text = viewModel.currentSelectedTask.taskTitle,
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
            Divider()

            Text(text = viewModel.currentSelectedTask.taskDescription,
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(scrollState)
                    .fillMaxWidth()
                    .weight(1f)

            )

            ExtendedFloatingActionButton(
                modifier = Modifier
                    .padding(end = 16.dp, bottom = 16.dp)
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.End),
                text = { Text("Delete task") },
                onClick = {
                    viewModel.deleteTask(viewModel.currentSelectedTask)
                    scope.launch { bottomSheetState.hide() }
                },
                icon = { Icon(Icons.Default.DeleteForever, null) }
            )

        },
        content = {
            Scaffold(
                floatingActionButton = {
                    FloatingActionButton(
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    x = 0,
                                    y = -(appBarOffsetHeightPx.value.roundToInt()) / 4
                                )
                            },
                        onClick = onAddTaskPressed
                    ) {
                        Icon(Icons.Default.Add, null)
                    }
                },
                isFloatingActionButtonDocked = true,
                floatingActionButtonPosition = FabPosition.Center,
                bottomBar = {
                    BottomAppBar(
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    x = 0,
                                    y = -(appBarOffsetHeightPx.value.roundToInt())
                                )
                            },
                        cutoutShape = RoundedCornerShape(percent = 50),
                        content = {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .wrapContentSize(Alignment.CenterEnd)
                            ) {

                                Box {
                                    IconButton(
                                        onClick = { viewModel.updateMenuDeletionExpandedState(!viewModel.menuDeletionExpandedState) }
                                    ) {
                                        Icon(Icons.Default.Delete, null)
                                    }

                                    DropdownMenuTaskDeletion(
                                        expanded = viewModel.menuDeletionExpandedState,
                                        setExpanded = { viewModel.updateMenuDeletionExpandedState(it) },
                                        onAllTasksPress = {
                                            viewModel.updateShowDeleteAllTasksDialog(true)
                                        },
                                        onCompletedTasksPress = {
                                            viewModel.updateShowDeleteCompletedTasksDialog(true)
                                        }
                                    )
                                }

                                IconButton(
                                    onClick = { viewModel.updateSearchBarState(!viewModel.searchBarState) }
                                ) {
                                    Icon(Icons.Default.Search, null)
                                }

                                Box {
                                    IconButton(
                                        onClick = { viewModel.updateMenuFiltersExpandedState(!viewModel.menuFiltersExpandedState) }
                                    ) {
                                        Icon(Icons.Default.FilterList, null)
                                    }

                                    DropdownMenuFilterSelection(
                                        expanded = viewModel.menuFiltersExpandedState,
                                        setExpanded = { viewModel.updateMenuFiltersExpandedState(it) },
                                        onFilterChange = viewModel::changeFilter
                                    )
                                }

                            }
                        }
                    )
                }
            ) {

                Column(Modifier.padding(it)) {
                    SearchBar(
                        isEnabled = viewModel.searchBarState,
                        text = viewModel.searchQuery,
                        setText = { viewModel.updateSearchQuery(it) },
                        onSearchPress = { viewModel.changeFilter(TaskFilterType.SEARCH(query = it)) },
                        onClosePress = {
                            viewModel.updateSearchBarState(false)
                            viewModel.changeFilter(TaskFilterType.ALL)
                        },
                        onClearQueryPress = { viewModel.updateSearchQuery("") }
                    )

                    if (viewModel.showDeleteAllTasksDialog) {
                        AlertDialogTaskDeletion(
                            title = "Delete",
                            message = "Do you want to delete all your tasks?",
                            onConfirmPressed = {
                                viewModel.clearAll()
                                viewModel.updateShowDeleteAllTasksDialog(false)
                            },
                            onDismissPressed = {
                                viewModel.updateShowDeleteAllTasksDialog(false)
                            }
                        )
                    }
                    if (viewModel.showDeleteCompletedTasksDialog) {
                        AlertDialogTaskDeletion(
                            title = "Delete",
                            message = "Do you want to delete all your completed tasks?",
                            onConfirmPressed = {
                                viewModel.clearCompletedTasks()
                                viewModel.updateShowDeleteCompletedTasksDialog(false)
                            },
                            onDismissPressed = {
                                viewModel.updateShowDeleteCompletedTasksDialog(false)
                            }
                        )
                    }

                    LazyColumn(
                        modifier = Modifier
                            .nestedScroll(nestedScrollConnection)
                    ) {
                        items(tasks.itemCount) { task ->
                            task?.let {
                                TaskItemView(
                                    modifier = Modifier
                                        .clickable {
                                            viewModel.updateCurrentSelectedTask(tasks[task]!!)
                                            scope.launch { bottomSheetState.show() }
                                        }
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    task = tasks[task]!!,
                                    onCheckboxPressed = { checked ->
                                        viewModel.updateTask(tasks[task]!!, checked)
                                    }
                                )
                                Divider()
                            }
                        }
                    }
                }

            }
        }
    )
}

@Composable
fun DropdownMenuTaskDeletion(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    setExpanded: (Boolean) -> Unit,
    onAllTasksPress: () -> Unit,
    onCompletedTasksPress: () -> Unit,
) {
    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        onDismissRequest = { setExpanded(false) }
    ) {
        DropdownMenuItem(
            onClick = {
                onAllTasksPress()
                setExpanded(false)
            }
        ) {
            Text(text = stringResource(R.string.menu_clear_tasks))
        }
        DropdownMenuItem(
            onClick = {
                onCompletedTasksPress()
                setExpanded(false)
            }
        ) {
            Text(text = stringResource(R.string.menu_clear_completed_tasks))
        }
    }
}

@Composable
fun DropdownMenuFilterSelection(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    setExpanded: (Boolean) -> Unit,
    onFilterChange: (TaskFilterType) -> Unit
) {

    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        onDismissRequest = { setExpanded(false) }
    ) {

        DropdownMenuItem(
            onClick = {
                onFilterChange(TaskFilterType.ALL)
                setExpanded(false)
            }
        ) {
            Text(text = stringResource(R.string.menu_all_tasks))
        }
        DropdownMenuItem(
            onClick = {
                onFilterChange(TaskFilterType.UNCOMPLETED)
                setExpanded(false)
            }
        ) {
            Text(text = stringResource(R.string.menu_uncompleted_tasks))
        }
        DropdownMenuItem(
            onClick = {
                onFilterChange(TaskFilterType.COMPLETED)
                setExpanded(false)
            }
        ) {
            Text(text = stringResource(R.string.menu_completed_tasks))
        }

    }


}