package com.nyanc0_android.mhwarmorcollection.infrastructure.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favorite(
    @PrimaryKey
    val armorId: String,
    val armorName: String,
    val monsterName: String,
    val parts: String
)