package com.sample.nyanc0_android.adjustablewrapcontentviewpager

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager

class AdjustableWrapContentViewPager(context: Context, attrs: AttributeSet? = null) :
    ViewPager(context, attrs) {

    private var pageHeightMap = mutableMapOf<Int, Int>()
    private var currentPage = 0

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
                // set currentPage to refresh view
                currentPage = position
                requestLayout()
            }
        })
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // call onMeasure to create child View
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

            pageHeightMap[currentPage]?.let {
                newHeightMeasureSpec =
                    MeasureSpec.makeMeasureSpec(it, MeasureSpec.EXACTLY)
            }

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