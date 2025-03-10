package com.fetchrewards.samplelist.sortedlist.data.repository

import com.fetchrewards.samplelist.R
import com.fetchrewards.samplelist.sortedlist.data.model.api.ResponseItem
import com.fetchrewards.samplelist.sortedlist.data.model.domain.FetchItemsResult
import com.fetchrewards.samplelist.sortedlist.data.model.domain.Item
import com.fetchrewards.samplelist.sortedlist.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for fetching a list of items from the network.
 */
@Singleton
class DefaultItemsRepository @Inject constructor(
    private val apiService: ApiService
): ItemsRepository {

    /**
     * Fetches a list of items and return a [FetchItemsResult].
     */
    override suspend fun fetchItems(): FetchItemsResult = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getItems()

            return@withContext if (response.isSuccessful) {
                response.body()?.let { items ->
                    if (items.isEmpty()) {
                        return@let FetchItemsResult.Failure(R.string.error_empty_list)
                    }
                    FetchItemsResult.Success(items.toDomainModel())
                } ?: FetchItemsResult.Failure(R.string.error_generic)
            } else {
                FetchItemsResult.Failure(R.string.error_generic)
            }
        } catch (e: SocketTimeoutException) {
            FetchItemsResult.Failure(R.string.error_network)
        } catch (e: ConnectException) {
            FetchItemsResult.Failure(R.string.error_network)
        }
    }
}

/**
 * Transforms a List<ResponseItem> api model to a List<Item> domain model sorted by groupId first
 * and name second. Invalid data is filtered out.
 */
fun List<ResponseItem>.toDomainModel(): List<Item> {
    return this
        // Filter out invalid data
        .filter {
            !it.name.isNullOrBlank()
                    && (it.id != null && it.id >= 0)
                    && (it.groupId != null && it.groupId >= 0)
        }
        // Sort by groupId/listId first, and then by name secondarily.
        .sortedWith(compareBy<ResponseItem> { it.groupId }
            .thenBy {
                it.name?.trim()?.substringAfter(' ')?.toInt()
            })
        // Transforms List<ResponseItem> to List<Item>.
        .map {
            Item(
                id = it.id ?: -1,
                groupId = it.groupId ?: -1,
                name = it.name ?: ""
            )
        }
}