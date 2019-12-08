package com.sample.nyanc0_android.wrapcontentviewpager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.sample.nyanc0_android.adjustablewrapcontentviewpager.AdjustableWrapContentViewPager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data = create()
        val pagerAdapter = WrapContentViewPagerAdapter(data)
        val pager = findViewById<ViewPager>(R.id.pager) as AdjustableWrapContentViewPager
        pager.setPageLimit(data.size)
        pager.adapter = pagerAdapter
    }

    private fun create(): List<WrapContentViewPagerAdapter.ViewModel> {
        return listOf(
            WrapContentViewPagerAdapter.ViewModel(
                "Title1",
                "ContentContentContentContentContentContentContentContentContentContentContent",
                listOf("Item1")
            ),
            WrapContentViewPagerAdapter.ViewModel(
                "Title2",
                "ContentContentContentContentContentContentContentContentContentContentContent",
                listOf("Item1", "Item2", "Item3", "Item4")
            ),
            WrapContentViewPagerAdapter.ViewModel(
                "Title3",
                "ContentContentContentContentContentContentContentContentContentContentContent",
                listOf("Item1")
            ),
            WrapContentViewPagerAdapter.ViewModel(
                "Title4",
                "content4content4content4content4content4content4content4",
                emptyList()
            ),
            WrapContentViewPagerAdapter.ViewModel(
                "Title5",
                "ContentContentContentContentContentContentContentContentContentContentContent",
                listOf("Item1", "Item2", "Item3")
            )
        )
    }
}
