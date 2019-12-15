package com.nyanc0_android.mhwarmorcollection.ui.monsterlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nyanc0_android.mhwarmorcollection.R
import com.nyanc0_android.mhwarmorcollection.infrastructure.model.Monster
import kotlinx.android.synthetic.main.item_monster.view.*

class MonsterListRecyclerAdapter(
    monsters: List<Monster>,
    private val onClickItem: (monster: Monster) -> Unit
) :
    RecyclerView.Adapter<MonsterListRecyclerAdapter.VH>() {

    private var monsterList = monsters.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_monster, parent, false)
        return VH(view)
    }

    override fun getItemCount(): Int = monsterList.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(monsterList[position], this::onClickItem)
    }

    fun refresh(monsters: List<Monster>) {
        monsterList.clear()
        monsterList.addAll(monsters)
        notifyDataSetChanged()
    }

    private fun onClickItem(monster: Monster) {
        onClickItem.invoke(monster)
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(monster: Monster, onClickItem: (monster: Monster) -> Unit) {
            itemView.text_name.text = monster.name
            itemView.setOnClickListener {
                onClickItem.invoke(monster)
            }
        }
    }
}