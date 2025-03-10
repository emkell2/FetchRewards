package com.fetchrewards.samplelist.sortedlist.data.model.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseItem(
    val id: Int? = -1,
    @SerialName("listId") val groupId: Int? = -1,
    val name: String? = ""
)
