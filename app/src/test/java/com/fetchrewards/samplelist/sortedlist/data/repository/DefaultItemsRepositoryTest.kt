package com.fetchrewards.samplelist.sortedlist.data.repository

import com.fetchrewards.samplelist.sortedlist.data.model.api.ResponseItem
import com.fetchrewards.samplelist.sortedlist.data.model.domain.FetchItemsResult
import com.fetchrewards.samplelist.sortedlist.network.ApiService
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response

class DefaultItemsRepositoryTest {
    private lateinit var apiService: ApiService
    private lateinit var subject: DefaultItemsRepository

    @BeforeEach
    fun setup() {
        apiService = mock()
        subject = DefaultItemsRepository(apiService)
    }

    @Test
    fun `when api call is successful and list is empty, expect failure returned`() =
        runTest {
            whenever(apiService.getItems()).thenReturn(
                Response.success(listOf())
            )
            val response = subject.fetchItems()
            assertTrue(response is FetchItemsResult.Failure)
        }

    @Test
    fun `when api call is successful and body is null, expect failure returned`() =
        runTest {
            whenever(apiService.getItems()).thenReturn(
                Response.success(null)
            )
            val response = subject.fetchItems()
            assertTrue(response is FetchItemsResult.Failure)
        }

    @Test
    fun `when api call is successful and list is NOT empty, expect success returned`() =
        runTest {
            whenever(apiService.getItems()).thenReturn(
                Response.success(ITEM_LIST_API)
            )
            val response = subject.fetchItems()
            assertTrue(response is FetchItemsResult.Success)
        }

    @Test
    fun `when api call is a failure, expect failure returned`() =
        runTest {
            val errorBody: ResponseBody = mock()
            val errorResponse: Response<List<ResponseItem>> =
                Response.error(DUMMY_ERROR_STATUS_CODE, errorBody)
            whenever(apiService.getItems()).thenReturn(
                errorResponse
            )
            val response = subject.fetchItems()
            assertTrue(response is FetchItemsResult.Failure)
        }

    @Test
    fun `when api model has empty string name, expect not in domain model`() {
        val subject = listOf(
            ResponseItem(
                id = 1,
                groupId = 1,
                name = ""
            )
        )
        assertTrue(subject.toDomainModel().isEmpty())
    }

    @Test
    fun `when api model has null name, expect not in domain model`() {
        val subject = listOf(
            ResponseItem(
                id = 1,
                groupId = 1,
                name = null
            )
        )
        assertTrue(subject.toDomainModel().isEmpty())
    }

    @Test
    fun `when api model has a null id, expect not in domain model`() {
        val subject = listOf(
            ResponseItem(
                id = null,
                groupId = 1,
                name = DUMMY_NAME
            )
        )
        assertTrue(subject.toDomainModel().isEmpty())
    }

    @Test
    fun `when api model has a null groupId, expect not in domain model`() {
        val subject = listOf(
            ResponseItem(
                id = 1,
                groupId = null,
                name = DUMMY_NAME
            )
        )
        assertTrue(subject.toDomainModel().isEmpty())
    }

    fun `when api model has a negative id, expect not in domain model`() {
        val subject = listOf(
            ResponseItem(
                id = -1,
                groupId = 1,
                name = DUMMY_NAME
            )
        )
        assertTrue(subject.toDomainModel().isEmpty())
    }

    fun `when api model has a negative groupId, expect not in domain model`() {
        val subject = listOf(
            ResponseItem(
                id = 1,
                groupId = -1,
                name = DUMMY_NAME
            )
        )
        assertTrue(subject.toDomainModel().isEmpty())
    }

    fun `when api model has a positive Int id, positive int groupId, and valid name, expect in domain model`() {
        assertTrue(ITEM_LIST_API.toDomainModel().isNotEmpty())
    }

    private companion object {
        const val DUMMY_NAME = "item 1"
        const val DUMMY_ERROR_STATUS_CODE = 500
        val ITEM_LIST_API = listOf(
            ResponseItem(
                id = 1,
                groupId = 1,
                name = DUMMY_NAME
            )
        )
    }
}