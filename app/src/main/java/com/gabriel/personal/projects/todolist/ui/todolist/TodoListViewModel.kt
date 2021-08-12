package com.gabriel.personal.projects.todolist.ui.todolist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gabriel.personal.projects.todolist.data.db.entity.Task
import com.gabriel.personal.projects.todolist.data.source.repository.TaskRepository
import com.gabriel.personal.projects.todolist.enums.TaskFilterType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(private val repository: TaskRepository) : ViewModel() {

    var currentSelectedTask by mutableStateOf(Task(taskTitle = "quick fix", taskDescription = "lol"))
        private set

    var menuFiltersExpandedState by mutableStateOf(false)
        private set

    var menuDeletionExpandedState by  mutableStateOf(false)
        private set

    var searchBarState by mutableStateOf(false)
        private set

    var searchQuery by mutableStateOf("")
        private set

    var showDeleteAllTasksDialog by mutableStateOf(false)
        private set

    var showDeleteCompletedTasksDialog by mutableStateOf(false)
        private set

    fun updateCurrentSelectedTask(task: Task) {
        currentSelectedTask = task
    }

    fun updateMenuFiltersExpandedState(value: Boolean) {
        menuFiltersExpandedState = value
    }
    fun updateMenuDeletionExpandedState(value: Boolean) {
        menuDeletionExpandedState = value
    }
    fun updateSearchBarState(value: Boolean) {
        searchBarState = value
    }
    fun updateSearchQuery(value: String) {
        searchQuery = value
    }
    fun updateShowDeleteAllTasksDialog(value: Boolean) {
        showDeleteAllTasksDialog = value
    }
    fun updateShowDeleteCompletedTasksDialog(value: Boolean) {
        showDeleteCompletedTasksDialog = value
    }

    private val _currentTaskFilterType = MutableStateFlow<TaskFilterType>(TaskFilterType.ALL)

    //filtered tasks exposed to fragment
    @ExperimentalCoroutinesApi
    val tasks: Flow<PagingData<Task>> = _currentTaskFilterType.flatMapLatest { filter ->
        when (filter) {
            is TaskFilterType.ALL -> {
                Pager(
                    config = PagingConfig(pageSize = 32, maxSize = 256)
                ) {
                    repository.getAll()
                }.flow.cachedIn(viewModelScope)
            }
            is TaskFilterType.COMPLETED -> {
                Pager(
                    config = PagingConfig(pageSize = 32, maxSize = 256)
                ) {
                    repository.getByCompleteState(true)
                }.flow.cachedIn(viewModelScope)
            }
            is TaskFilterType.UNCOMPLETED -> {
                Pager(
                    config = PagingConfig(pageSize = 32, maxSize = 256)
                ) {
                    repository.getByCompleteState(false)
                }.flow.cachedIn(viewModelScope)
            }
            is TaskFilterType.SEARCH -> {
                Pager(
                    config = PagingConfig(pageSize = 32, maxSize = 256)
                ) {
                    repository.searchByName("%${filter.query}%")
                }.flow.cachedIn(viewModelScope)
            }
        }
    }

    fun changeFilter(taskFilterType: TaskFilterType) {
        _currentTaskFilterType.value = taskFilterType
    }

    fun deleteTask(task: Task) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTask(task)
        }


    fun updateTask(task: Task, isComplete: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateComplete(task.taskId, isComplete)
        }


    fun clearAll() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.clear()
        }


    fun clearCompletedTasks() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearCompletedTasks()
        }


}

