package com.fetchrewards.samplelist.sortedlist.viewmodel

import androidx.lifecycle.ViewModel
import com.fetchrewards.samplelist.sortedlist.data.repository.ItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.fetchrewards.samplelist.R
import com.fetchrewards.samplelist.sortedlist.data.model.domain.FetchItemsResult
import com.fetchrewards.samplelist.sortedlist.data.model.domain.Item
import com.fetchrewards.samplelist.sortedlist.network.NetworkMonitor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SortedListViewModel @Inject constructor(
    private val itemsRepository: ItemsRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _state = MutableStateFlow(SortedListScreenState())
    val state: StateFlow<SortedListScreenState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<SortedListScreenEvents>()
    val events: SharedFlow<SortedListScreenEvents> = _events.asSharedFlow()

    /**
     * Send an Action/Intent from the UI layer to the ViewModel.
     */
    fun sendAction(action: SortedListScreenAction) {
        when (action) {
            SortedListScreenAction.OnScreenLoad -> handleOnScreenLoadAction()
            SortedListScreenAction.OnErrorDialogBtnClick -> handleOnErrorDialogBtnClicked()
        }
    }

    private fun handleOnScreenLoadAction() {
        if (networkMonitor.isNetworkConnectivityAvailable()) {
            fetchItems()
        } else {
            updateStateWithError(R.string.error_no_network)
        }
    }

    private fun handleOnErrorDialogBtnClicked() {
        resetErrorState()
        viewModelScope.launch {
            _events.emit(SortedListScreenEvents.NavigateToInitialScreen)
        }
    }

    private fun fetchItems() {
        updateStateIsLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            val result = itemsRepository.fetchItems()
            updateStateIsLoading(false)
            when (result) {
                is FetchItemsResult.Success -> updateStateWithItems(result.items)
                is FetchItemsResult.Failure -> updateStateWithError(result.errorResId)
            }
        }
    }

    private fun resetErrorState() {
        _state.update {
            it.copy(
                hasError = false,
                errorTitleResId = null,
                errorDescriptionResId = null
            )
        }
    }

    private fun updateStateWithError(errorResId: Int) {
        _state.update {
            it.copy(
                hasError = true,
                errorTitleResId = R.string.error_title,
                errorDescriptionResId = errorResId
            )
        }
    }

    private fun updateStateWithItems(items: List<Item>) {
        val groupedItems = items.groupBy { it.groupId }
        _state.update {
            it.copy(
                items = groupedItems
            )
        }
    }

    private fun updateStateIsLoading(isLoading: Boolean) {
        _state.update {
            it.copy(isLoading = isLoading)
        }
    }
}

/**
 * Represents the state of the UI for the SortedListScreen.
 */
data class SortedListScreenState(
    val items: Map<Int, List<Item>> = mapOf(),
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    val errorTitleResId: Int? = null,
    val errorDescriptionResId: Int? = null
)

/**
 * Actions that are sent from the UI to the Viewmodel.
 */
sealed class SortedListScreenAction {
    data object OnScreenLoad : SortedListScreenAction()
    data object OnErrorDialogBtnClick : SortedListScreenAction()
}

/**
 * Events that are sent from the ViewModel to the UI layer.
 */
sealed class SortedListScreenEvents {
    data object NavigateToInitialScreen : SortedListScreenEvents()
}

