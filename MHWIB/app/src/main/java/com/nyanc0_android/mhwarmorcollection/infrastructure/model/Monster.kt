package com.nyanc0_android.mhwarmorcollection.infrastructure.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Monster(
    val id: String,
    val name: String
) : Parcelable