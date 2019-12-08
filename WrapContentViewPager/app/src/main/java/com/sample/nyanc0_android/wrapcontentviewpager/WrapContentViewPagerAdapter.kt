package com.sample.nyanc0_android.wrapcontentviewpager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.sample.nyanc0_android.wrapcontentviewpager.databinding.ItemRecyclerBinding
import com.sample.nyanc0_android.wrapcontentviewpager.databinding.ViewPageBinding

class WrapContentViewPagerAdapter(private val data: List<ViewModel>) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding =
            ViewPageBinding.inflate(LayoutInflater.from(container.context), container, false)
        binding.vm = data[position]
        binding.recycler.adapter = SampleRecyclerAdapter(data[position].list)
        binding.btnAdd.setOnClickListener {

            val adapter = binding.recycler.adapter as SampleRecyclerAdapter

            if (data[position].isChecked) {
                // false
                adapter.remove()
            } else {
                // true
                adapter.add("ItemAdded")
            }
            data[position].isChecked = !data[position].isChecked
        }
        container.addView(binding.root)
        return binding.root
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = data.size

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    class ViewModel(
        val title: String,
        val content: String,
        val list: List<String>,
        var isChecked: Boolean = false
    )

    class SampleRecyclerAdapter(list: List<String>) :
        RecyclerView.Adapter<SampleRecyclerAdapter.VH>() {

        private var data = list.toMutableList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val binding = DataBindingUtil.inflate<ItemRecyclerBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_recycler,
                parent,
                false
            )
            return VH(binding)
        }

        override fun getItemCount(): Int = data.size

        override fun onBindViewHolder(holder: VH, position: Int) {
            holder.itemSampleBinding.title.text = data[position]
        }

        fun remove() {
            if (data.isNotEmpty()) {
                data.removeAt(0)
                notifyDataSetChanged()
            }
        }

        fun add(item: String) {
            data.add(item)
            notifyDataSetChanged()
        }

        class VH(val itemSampleBinding: ItemRecyclerBinding) :
            RecyclerView.ViewHolder(itemSampleBinding.root)
    }

}