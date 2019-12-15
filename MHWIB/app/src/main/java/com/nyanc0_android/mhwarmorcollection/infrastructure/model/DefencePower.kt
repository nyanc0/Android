package com.nyanc0_android.mhwarmorcollection.infrastructure.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DefencePower(
    val normal: Long,
    val custom: Long
): Parcelable