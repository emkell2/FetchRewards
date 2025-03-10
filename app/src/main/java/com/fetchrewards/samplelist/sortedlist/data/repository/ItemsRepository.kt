package com.fetchrewards.samplelist.sortedlist.data.repository

import com.fetchrewards.samplelist.sortedlist.data.model.domain.FetchItemsResult

interface ItemsRepository {
    suspend fun fetchItems(): FetchItemsResult
}