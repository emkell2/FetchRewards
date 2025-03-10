package com.fetchrewards.samplelist.sortedlist.data.model.domain

sealed class FetchItemsResult {
    data class Success(val items: List<Item>) : FetchItemsResult()
    data class Failure(val errorResId: Int) : FetchItemsResult()
}