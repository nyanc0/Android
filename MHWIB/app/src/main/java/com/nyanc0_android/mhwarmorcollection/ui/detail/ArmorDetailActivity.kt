package com.nyanc0_android.mhwarmorcollection.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.nyanc0_android.mhwarmorcollection.R
import com.nyanc0_android.mhwarmorcollection.infrastructure.model.*
import com.nyanc0_android.mhwarmorcollection.infrastructure.repository.ArmorIdRepository
import com.nyanc0_android.mhwarmorcollection.infrastructure.repository.FavoriteRepository
import kotlinx.android.synthetic.main.activity_armor_detail.*
import kotlinx.android.synthetic.main.item_detail_armor_name.view.*
import kotlinx.android.synthetic.main.item_detail_attribute.view.*
import kotlinx.android.synthetic.main.item_detail_defence.view.*
import kotlinx.android.synthetic.main.item_detail_label.view.*
import kotlinx.android.synthetic.main.item_detail_skill.view.*
import kotlinx.android.synthetic.main.item_detail_slot.view.*
import kotlinx.android.synthetic.main.item_material.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ArmorDetailActivity : AppCompatActivity() {

    private lateinit var armor: Armor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_armor_detail)

        intent.extras?.getParcelable<Armor>(KEY)?.let {
            armor = it
            showDetail(it)
        } ?: checkArmorId()
    }

    private fun showDetail(armor: Armor) {
        layout_armor_name.text_armor_name.text = armor.name
        layout_armor_name.text_parts.text = armor.parts

        val inflater = LayoutInflater.from(this)
        initFav(armor.id)
        initSkill(armor.skill, inflater)
        initSlot(armor.slot)
        initDefence(armor.defencePower, inflater)
        initAttribute(armor.attribute, inflater)
        initMaterial(armor.material, inflater)
    }

    private fun initSkill(skills: List<Skill>, inflater: LayoutInflater) {
        label_skill.text_label.text = getString(R.string.label_detail_skill)
        container_skill.removeAllViews()
        for (skill in skills) {
            val skillView = inflater.inflate(R.layout.item_detail_skill, container_skill, false)
            skillView.text_skill_name.text = skill.name
            skillView.text_skill_lv.text = getString(R.string.label_detail_skill_lv, skill.lv)
            container_skill.addView(skillView)
        }
    }

    private fun initSlot(slots: Slot) {
        label_slot.text_label.text = getString(R.string.label_detail_slot)
        if (slots.values.isEmpty()) {
            layout_slot.text_slot.text = getString(R.string.label_detail_slot_num, 0, 0, 0)
        } else {
            layout_slot.text_slot.text = getString(
                R.string.label_detail_slot_num,
                slots.values[0],
                slots.values[1],
                slots.values[2]
            )
        }
    }

    private fun initDefence(defencePower: DefencePower, inflater: LayoutInflater) {
        label_defence.text_label.text = getString(R.string.label_detail_defence)
        container_defence.removeAllViews()
        val normal = inflater.inflate(R.layout.item_detail_defence, container_defence, false)
        normal.text_defence_name.text = getString(R.string.label_defence_normal)
        normal.text_defence_power.text =
            getString(R.string.label_detail_defence_num, defencePower.normal)
        container_defence.addView(normal)

        val custom = inflater.inflate(R.layout.item_detail_defence, container_defence, false)
        custom.text_defence_name.text = getString(R.string.label_defence_normal)
        custom.text_defence_power.text =
            getString(R.string.label_detail_defence_num, defencePower.custom)
        container_defence.addView(custom)
    }

    private fun initAttribute(attribute: Attribute, inflater: LayoutInflater) {
        label_attribute.text_label.text = getString(R.string.label_detail_attribute)

        container_attribute.removeAllViews()
        val fire = inflater.inflate(R.layout.item_detail_attribute, container_attribute, false)
        fire.text_attribute_name.text = getString(R.string.label_detail_attribute_name, "火")
        fire.text_attribute_lv.text = getString(R.string.label_detail_attribute_lv, attribute.fire)

        val water = inflater.inflate(R.layout.item_detail_attribute, container_attribute, false)
        water.text_attribute_name.text = getString(R.string.label_detail_attribute_name, "水")
        water.text_attribute_lv.text =
            getString(R.string.label_detail_attribute_lv, attribute.water)

        val lightning = inflater.inflate(R.layout.item_detail_attribute, container_attribute, false)
        lightning.text_attribute_name.text = getString(R.string.label_detail_attribute_name, "雷")
        lightning.text_attribute_lv.text =
            getString(R.string.label_detail_attribute_lv, attribute.lightning)

        val ice = inflater.inflate(R.layout.item_detail_attribute, container_attribute, false)
        ice.text_attribute_name.text = getString(R.string.label_detail_attribute_name, "氷")
        ice.text_attribute_lv.text = getString(R.string.label_detail_attribute_lv, attribute.ice)

        val dragon = inflater.inflate(R.layout.item_detail_attribute, container_attribute, false)
        dragon.text_attribute_name.text = getString(R.string.label_detail_attribute_name, "龍")
        dragon.text_attribute_lv.text =
            getString(R.string.label_detail_attribute_lv, attribute.dragon)

        container_attribute.addView(fire)
        container_attribute.addView(water)
        container_attribute.addView(lightning)
        container_attribute.addView(ice)
        container_attribute.addView(dragon)
    }

    private fun initMaterial(materials: List<Material>, inflater: LayoutInflater) {
        label_material.text_label.text = getString(R.string.label_detail_material)
        container_material.removeAllViews()

        for (material in materials) {
            val view = inflater.inflate(R.layout.item_material, container_material, false)
            view.text_material_name.text = material.name
            view.text_material_quantity.text =
                getString(R.string.label_detail_material_num, material.quantity)
            container_material.addView(view)
        }
    }

    private fun initFav(armorId: String) {
        GlobalScope.launch(Dispatchers.Main) {
            val repository = FavoriteRepository(applicationContext)
            val favorite = repository.getFavorite(armorId)
            button_fav.isChecked = favorite != null
            button_fav.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    repository.saveFavorite(armor)
                } else {
                    repository.deleteFavorite(armor)
                }
            }
        }
    }

    private fun checkArmorId() {
        intent.extras?.getString(KEY_ID)?.let {
            fetchArmor(it)
        } ?: showError()
    }

    private fun showError() {

    }

    private fun fetchArmor(armorId: String) = GlobalScope.launch(Dispatchers.Main) {
        val repository = ArmorIdRepository()
        val result = repository.fetchOne(armorId)
        result?.let {
            armor = it
            showDetail(it)
        } ?: showError()
    }

    companion object {
        fun intent(activity: FragmentActivity, armor: Armor) =
            Intent(activity, ArmorDetailActivity::class.java).also {
                it.putExtra(KEY, armor)
            }

        fun intent(activity: FragmentActivity, armorId: String) =
            Intent(activity, ArmorDetailActivity::class.java).also {
                it.putExtra(KEY_ID, armorId)
            }

        private const val KEY = "armor"
        private const val KEY_ID = "armorId"
    }
}