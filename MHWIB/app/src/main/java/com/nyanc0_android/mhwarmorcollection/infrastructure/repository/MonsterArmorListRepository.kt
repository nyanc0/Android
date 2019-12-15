package com.nyanc0_android.mhwarmorcollection.infrastructure.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.nyanc0_android.mhwarmorcollection.infrastructure.mapper.mapToArmor
import com.nyanc0_android.mhwarmorcollection.infrastructure.model.Armor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MonsterArmorListRepository {

    suspend fun fetch(monsterName: String): List<Armor> {
        return withContext(Dispatchers.IO) {
            fetchInternal(monsterName)
        }
    }

    private suspend fun fetchInternal(monsterName: String): List<Armor> = suspendCoroutine {
        val result = mutableListOf<Armor>()
        val db = FirebaseFirestore.getInstance()
        db.collection(COLLECTION_PATH)
            .whereEqualTo(QUERY, monsterName)
            .get()
            .addOnSuccessListener { snapShot ->
                for (document in snapShot) {
                    result.add(mapToArmor(document.data))
                }
                it.resume(result)
            }.addOnFailureListener { exception ->
                it.resumeWithException(exception)
            }
    }

    companion object {
        private const val COLLECTION_PATH = "ArmorCollection"
        private const val QUERY = "monsterName"
    }
}