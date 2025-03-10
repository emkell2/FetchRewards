package com.fetchrewards.samplelist.sortedlist.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.fetchrewards.samplelist.sortedlist.ui.screens.composables.LoadingIndicator
import com.fetchrewards.samplelist.sortedlist.ui.screens.composables.SortedListScreenUIContent
import com.fetchrewards.samplelist.sortedlist.viewmodel.SortedListScreenAction
import com.fetchrewards.samplelist.sortedlist.viewmodel.SortedListScreenEvents
import com.fetchrewards.samplelist.sortedlist.viewmodel.SortedListViewModel

@Composable
fun SortedListScreen(
    viewModel: SortedListViewModel = hiltViewModel(),
    onNavigateInitialScreen: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.sendAction(SortedListScreenAction.OnScreenLoad)

        viewModel.events.collect { event ->
            when (event) {
                SortedListScreenEvents.NavigateToInitialScreen -> { onNavigateInitialScreen() }
            }
        }
    }

    if (state.isLoading) {
        LoadingIndicator()
    }

    SortedListScreenUIContent(
        state = state,
        onErrorDialogDismiss = { viewModel.sendAction(SortedListScreenAction.OnErrorDialogBtnClick) }
    )
}