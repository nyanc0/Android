package com.nyanc0_android.mhwarmorcollection.ui.record

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nyanc0_android.mhwarmorcollection.R
import com.nyanc0_android.mhwarmorcollection.infrastructure.model.Armor
import kotlinx.android.synthetic.main.item_record.view.*

class RecordListRecyclerAdapter(armors: List<Armor>) :
    RecyclerView.Adapter<RecordListRecyclerAdapter.VH>() {

    private var vmList = mutableListOf<VM>()

    init {
        vmList = armors.map {
            VM(it.name, it.parts, it.monsterName, false)
        }.toMutableList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_record, parent, false)
        return VH(view)
    }

    override fun getItemCount(): Int = vmList.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(vmList[position])
    }

    data class VM(
        var name: String = "",
        var parts: String = "",
        var monsterName: String = "",
        var isEdit: Boolean = false
    )

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(vm: VM) {
            if (vm.isEdit) {
                itemView.edit_text_parts.setText(vm.parts)
                itemView.edit_text_armor_name.setText(vm.name)
                itemView.edit_text_monster_name.setText(vm.monsterName)

                itemView.edit_text_parts.visibility = View.VISIBLE
                itemView.edit_text_armor_name.visibility = View.VISIBLE
                itemView.edit_text_monster_name.visibility = View.VISIBLE

                itemView.text_parts.visibility = View.GONE
                itemView.text_armor_name.visibility = View.GONE
                itemView.text_monster_name.visibility = View.GONE
            } else {
                itemView.text_parts.text = vm.parts
                itemView.text_armor_name.text = vm.name
                itemView.text_monster_name.text = vm.monsterName

                itemView.edit_text_parts.visibility = View.GONE
                itemView.edit_text_armor_name.visibility = View.GONE
                itemView.edit_text_monster_name.visibility = View.GONE

                itemView.text_parts.visibility = View.VISIBLE
                itemView.text_armor_name.visibility = View.VISIBLE
                itemView.text_monster_name.visibility = View.VISIBLE
            }

            itemView.btn_edit.setOnClickListener {
                if (vm.isEdit) {
                    // OFF
                    vm.name = itemView.edit_text_armor_name.text.toString()
                    vm.monsterName = itemView.edit_text_monster_name.text.toString()
                    vm.parts = itemView.edit_text_parts.text.toString()

                    itemView.text_parts.text = vm.parts
                    itemView.text_armor_name.text = vm.name
                    itemView.text_monster_name.text = vm.monsterName

                    itemView.edit_text_parts.visibility = View.GONE
                    itemView.edit_text_armor_name.visibility = View.GONE
                    itemView.edit_text_monster_name.visibility = View.GONE
                    itemView.text_parts.visibility = View.VISIBLE
                    itemView.text_armor_name.visibility = View.VISIBLE
                    itemView.text_monster_name.visibility = View.VISIBLE
                } else {
                    // ON
                    itemView.edit_text_parts.setText(vm.parts)
                    itemView.edit_text_armor_name.setText(vm.name)
                    itemView.edit_text_monster_name.setText(vm.monsterName)

                    itemView.edit_text_parts.visibility = View.VISIBLE
                    itemView.edit_text_armor_name.visibility = View.VISIBLE
                    itemView.edit_text_monster_name.visibility = View.VISIBLE

                    itemView.text_parts.visibility = View.GONE
                    itemView.text_armor_name.visibility = View.GONE
                    itemView.text_monster_name.visibility = View.GONE
                }
                vm.isEdit = !vm.isEdit
            }
        }
    }
}