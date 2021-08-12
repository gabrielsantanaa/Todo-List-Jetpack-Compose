package com.gabriel.personal.projects.todolist.ui.addtask

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabriel.personal.projects.todolist.data.db.entity.Task
import com.gabriel.personal.projects.todolist.data.source.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    var inputTitle by mutableStateOf("")
        private set

    var inputDescription by mutableStateOf("")
        private set

    fun updateInputTitle(title: String) {
        inputTitle = title
    }

    fun updateInputDescription(description: String) {
        inputDescription = description
    }

    private fun clearInputs() {
        inputTitle = ""
        inputDescription = ""
    }

    fun saveTask() =
        viewModelScope.launch(Dispatchers.IO) {
            if(inputTitle.isNotBlank() && inputDescription.isNotBlank()) {
                repository.insert(Task(taskTitle = inputTitle, taskDescription = inputDescription))
                clearInputs()
            }
        }


}