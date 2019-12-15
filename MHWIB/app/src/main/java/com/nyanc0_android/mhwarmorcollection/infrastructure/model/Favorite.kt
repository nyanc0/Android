package com.nyanc0_android.mhwarmorcollection.infrastructure.model

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Favorite(
    val armorId: String,
    val armorName: String,
    val monsterName: String,
    val parts: String
)


@JsonClass(generateAdapter = true)
data class FavoriteSet(
    val list: List<Favorite>
)