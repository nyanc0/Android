package com.nyanc0_android.mhwarmorcollection.ui.armorlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nyanc0_android.mhwarmorcollection.R
import com.nyanc0_android.mhwarmorcollection.infrastructure.model.Armor
import kotlinx.android.synthetic.main.item_armor.view.*

class ArmorListRecyclerAdapter(
    armors: List<Armor>,
    private val onClickItem: (armor: Armor) -> Unit
) :
    RecyclerView.Adapter<ArmorListRecyclerAdapter.VH>() {

    private val armorList = armors.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_armor, parent, false)
        return VH(view)
    }

    override fun getItemCount(): Int = armorList.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(armorList[position], this::onItemClick)
    }

    private fun onItemClick(armor: Armor) {
        onClickItem.invoke(armor)
    }

    fun refresh(armors: List<Armor>) {
        armorList.clear()
        armorList.addAll(armors)
        notifyDataSetChanged()
    }


    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(armor: Armor, onClickItem: (armor: Armor) -> Unit) {
            itemView.text_parts.text = armor.parts
            itemView.text_monster_name.text = armor.monsterName
            itemView.text_armor_name.text = armor.name
            itemView.setOnClickListener {
                onClickItem.invoke(armor)
            }
        }
    }
}