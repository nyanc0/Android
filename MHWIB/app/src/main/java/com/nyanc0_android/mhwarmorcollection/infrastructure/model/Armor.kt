package com.nyanc0_android.mhwarmorcollection.infrastructure.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Armor(
    val id: String,
    val name: String,
    val skill: List<Skill>,
    val slot: Slot,
    val defencePower: DefencePower,
    val attribute: Attribute,
    val material: List<Material>,
    val parts: String,
    val monsterName: String
) : Parcelable