package com.sample.nyanc0_android.adjustablewrapcontentviewpager

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager

class AdjustableWrapContentViewPager(context: Context, attrs: AttributeSet? = null) :
    ViewPager(context, attrs) {

    private var pageHeightMap = mutableMapOf<Int, Int>()
    var currentPage = 0

    init {
        addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                // nothing to do
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                // nothing to do
            }

            override fun onPageSelected(position: Int) {
                currentPage = position
                requestLayout()
            }
        })
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val mode = MeasureSpec.getMode(heightMeasureSpec)
        var newHeightMeasureSpec = 0
        if (mode == MeasureSpec.UNSPECIFIED || mode == MeasureSpec.AT_MOST) {
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                child.measure(
                    widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                )
                pageHeightMap[i] = child.measuredHeight
            }

            newHeightMeasureSpec =
                MeasureSpec.makeMeasureSpec(pageHeightMap[currentPage]!!, MeasureSpec.EXACTLY)

            if (newHeightMeasureSpec != 0) {
                super.onMeasure(widthMeasureSpec, newHeightMeasureSpec)
            } else {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            }
        }
    }

    fun setPageLimit(limit: Int) {
        offscreenPageLimit = limit
    }
}