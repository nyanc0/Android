@file:Suppress("UNCHECKED_CAST")

package com.nyanc0_android.mhwarmorcollection.infrastructure.mapper

import com.nyanc0_android.mhwarmorcollection.infrastructure.model.*

fun mapToArmor(obj: Map<String, Any>) =
    Armor(
        id = obj["armorId"] as String,
        name = obj["armorName"] as String,
        skill = createSkill(obj["skill"] as List<Map<String, Any>>),
        slot = Slot(obj["slot"] as List<Long>),
        defencePower = createDefencePower(obj["defencePower"] as Map<String, Long>),
        attribute = createAttribute(obj["attribute"] as Map<String, Long>),
        material = createMaterial(obj["material"] as List<Map<String, Any>>),
        parts = obj["parts"] as String,
        monsterName = obj["monsterName"] as String
    )

fun mapToMonster(obj: Map<String, Any>) =
    Monster(
        id = obj["monsterId"] as String,
        name = obj["monsterName"] as String
    )

fun createSkill(skills: List<Map<String, Any>>): List<Skill> {
    val list = mutableListOf<Skill>()
    skills.forEach {
        list.add(Skill(it["name"] as String, it["lv"] as Long))
    }
    return list
}

fun createDefencePower(defence: Map<String, Long>): DefencePower {
    return DefencePower(
        normal = defence["normal"] as Long,
        custom = defence["custom"] as Long
    )
}

fun createMaterial(materials: List<Map<String, Any>>): List<Material> {
    val list = mutableListOf<Material>()
    materials.forEach {
        list.add(Material(it["name"] as String, it["quantity"] as Long))
    }
    return list
}

fun createAttribute(attributes: Map<String, Long>): Attribute {
    return Attribute(
        attributes["fire"] as Long,
        attributes["water"] as Long,
        attributes["lightning"] as Long,
        attributes["ice"] as Long,
        attributes["dragon"] as Long
    )
}