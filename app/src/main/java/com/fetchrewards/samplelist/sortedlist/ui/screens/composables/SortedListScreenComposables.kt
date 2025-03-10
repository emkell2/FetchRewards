package com.fetchrewards.samplelist.sortedlist.ui.screens.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fetchrewards.samplelist.R
import com.fetchrewards.samplelist.sortedlist.viewmodel.SortedListScreenState

@Composable
fun SortedListScreenUIContent(
    state: SortedListScreenState,
    onErrorDialogDismiss: () -> Unit
) {
    Scaffold { padding ->
        if (state.hasError) {
            ErrorDialog(
                title = state.errorTitleResId,
                description = state.errorDescriptionResId,
                onDismiss = { onErrorDialogDismiss() }
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            state.items.forEach { (groupId, groupItems) ->
                // Section Header
                item {
                    Text(
                        text = stringResource(R.string.list_screen_section_header_text, groupId),
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .background(colorResource(R.color.purple_700))
                            .padding(8.dp)
                    )
                }

                // Section Items
                items(groupItems) { item ->
                    Text(
                        text = item.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp, horizontal = 20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.Center),
            color = colorResource(R.color.purple_700),
            strokeWidth = 4.dp
        )
    }
}

@Composable
fun ErrorDialog(
    title: Int?,
    description: Int?,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(title?.let { stringResource(it) } ?: "") },
        text = { Text(description?.let { stringResource(it) } ?: "") },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.error_dialog_ok_btn_text))
            }
        }
    )
}