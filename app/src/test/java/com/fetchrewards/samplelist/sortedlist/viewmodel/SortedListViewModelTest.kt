package com.fetchrewards.samplelist.sortedlist.viewmodel

import com.fetchrewards.samplelist.fakes.FakeItemsRepository
import com.fetchrewards.samplelist.fakes.FakeNetworkMonitor
import com.fetchrewards.samplelist.R
import com.fetchrewards.samplelist.sortedlist.data.model.domain.FetchItemsResult
import com.fetchrewards.samplelist.sortedlist.data.model.domain.Item
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class SortedListViewModelTest {
    private lateinit var subject: SortedListViewModel

    private val itemsRepository = FakeItemsRepository()
    private val networkMonitor = FakeNetworkMonitor()

    @BeforeEach
    fun setup() {
        subject = SortedListViewModel(
            itemsRepository = itemsRepository,
            networkMonitor = networkMonitor
        )
    }

    @Test
    fun `when OnScreenLoad action is sent, expect items are fetched when network is available`() =
        runTest {
            networkMonitor.isNetworkConnectivityAvailableReturn = true
            itemsRepository.fetchItemsResult = FetchItemsResult.Success(ITEMS_LIST)

            assertTrue(subject.state.value.items.isEmpty())

            subject.sendAction(SortedListScreenAction.OnScreenLoad)

            advanceUntilIdle()

            assertTrue(subject.state.value.items.isNotEmpty())
        }

    @Test
    fun `when OnScreenLoad action is sent, expect error when network is NOT available`() =
        runTest {
            networkMonitor.isNetworkConnectivityAvailableReturn = false

            subject.sendAction(SortedListScreenAction.OnScreenLoad)

            advanceUntilIdle()

            val state = subject.state.value
            assertTrue(state.items.isEmpty())
            assertTrue(state.hasError)
            assertEquals(R.string.error_title, state.errorTitleResId)
            assertEquals(R.string.error_no_network, state.errorDescriptionResId)
        }

    @Test
    fun `when OnErrorDialogBtnClick action is sent, expect state is updated properly and NavigateToInitialScreen event sent`() =
        runTest {
            val events = mutableListOf<SortedListScreenEvents>()
            val job = launch {
                subject.events.collect { event ->
                    events.add(event)
                }
            }

            subject.sendAction(SortedListScreenAction.OnErrorDialogBtnClick)
            advanceUntilIdle()

            // Check State is updated
            val state = subject.state.value
            assertTrue(state.items.isEmpty())
            assertFalse(state.hasError)
            assertNull(state.errorTitleResId)
            assertNull(state.errorDescriptionResId)

            // Check event fired
            assertEquals(listOf(SortedListScreenEvents.NavigateToInitialScreen), events)
            job.cancel()
        }

    private companion object {
        const val DUMMY_ID = 1
        const val DUMMY_GROUP_ID = 1
        const val DUMMY_NAME = "Item 1"

        val ITEMS_LIST = listOf(
            Item(
                id = DUMMY_ID,
                groupId = DUMMY_GROUP_ID,
                name = DUMMY_NAME
            ),
            Item(
                id = DUMMY_ID,
                groupId = DUMMY_GROUP_ID,
                name = DUMMY_NAME
            )
        )
    }
}