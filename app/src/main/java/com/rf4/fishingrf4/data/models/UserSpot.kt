package com.rf4.fishingrf4.data.models

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class UserSpot(
    val id: String = UUID.randomUUID().toString(),
    val lakeId: String, // ex: "kuori"
    val position: String, // ex: "45:55"
    val comment: String
)