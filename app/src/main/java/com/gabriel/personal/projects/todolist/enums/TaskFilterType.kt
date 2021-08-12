package com.gabriel.personal.projects.todolist.enums

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.gabriel.personal.projects.todolist.R

sealed class TaskFilterType {

    object ALL : TaskFilterType() {
        @Composable
        override fun getTitle(): String =
            stringResource(R.string.title_all_tasks)
    }

    object COMPLETED : TaskFilterType() {
        @Composable
        override fun getTitle(): String =
            stringResource(R.string.title_completed_tasks)
    }

    object UNCOMPLETED : TaskFilterType() {
        @Composable
        override fun getTitle(): String =
            stringResource(R.string.title_uncompleted_tasks)
    }

    class SEARCH(val query: String) : TaskFilterType() {
        @Composable
        override fun getTitle(): String =
            stringResource(R.string.search_toolbar_title, query)

    }

    @Composable
    abstract fun getTitle(): String

}

