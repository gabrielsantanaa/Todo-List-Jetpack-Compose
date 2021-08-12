package com.gabriel.personal.projects.todolist.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SearchBar(
    isEnabled: Boolean,
    text: String,
    setText: (String) -> Unit,
    onSearchPress: (String) -> Unit,
    onClearQueryPress: () -> Unit,
    onClosePress: () -> Unit
) {

    val enter = remember { fadeIn(animationSpec = TweenSpec(300, easing = FastOutLinearInEasing)) }
    val exit = remember { fadeOut(animationSpec = TweenSpec(100, easing = FastOutSlowInEasing)) }

    AnimatedVisibility(
        visible = isEnabled,
        enter = enter,
        exit = exit,
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = text,
                label = { Text("Search by name") },
                onValueChange = setText,
                leadingIcon = {
                    IconButton(
                        modifier = Modifier.padding(start = 4.dp),
                        onClick = onClosePress
                    ) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    if(text.isNotBlank()) onSearchPress(text)
                }),
                trailingIcon = {
                    if(text.isNotBlank()) {
                        IconButton(
                            modifier = Modifier.padding(start = 4.dp),
                            onClick = onClearQueryPress
                        ) {
                            Icon(Icons.Default.Close, null)
                        }
                    }
                },
                colors =  TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                )
            )
        }
    }

}