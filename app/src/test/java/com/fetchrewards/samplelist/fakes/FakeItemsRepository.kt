package com.fetchrewards.samplelist.fakes

import com.fetchrewards.samplelist.sortedlist.data.model.domain.FetchItemsResult
import com.fetchrewards.samplelist.sortedlist.data.repository.ItemsRepository

class FakeItemsRepository : ItemsRepository {
    var fetchItemsResult = FetchItemsResult.Success(listOf())
    override suspend fun fetchItems(): FetchItemsResult = fetchItemsResult
}