package com.nyanc0_android.mhwarmorcollection.common

import android.graphics.Rect

sealed class Graphic

data class BoxGraphic(val text: String, val boundingBox: Rect) : Graphic()
data class TextGraphic(val texts: List<String>) : Graphic()