package com.nyanc0_android.mhwarmorcollection.infrastructure.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Attribute(
    val fire: Long,
    val water: Long,
    val lightning: Long,
    val ice: Long,
    val dragon: Long
) : Parcelable