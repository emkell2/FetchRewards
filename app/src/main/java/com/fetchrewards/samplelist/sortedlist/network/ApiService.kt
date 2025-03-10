package com.fetchrewards.samplelist.sortedlist.network

import com.fetchrewards.samplelist.sortedlist.constants.ApiConstants.ITEMS_PATH
import com.fetchrewards.samplelist.sortedlist.data.model.api.ResponseItem
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET(ITEMS_PATH)
    suspend fun getItems(): Response<List<ResponseItem>>
}