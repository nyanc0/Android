package com.nyanc0_android.mhwarmorcollection.infrastructure.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.nyanc0_android.mhwarmorcollection.infrastructure.mapper.mapToArmor
import com.nyanc0_android.mhwarmorcollection.infrastructure.model.Armor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ArmorIdRepository {

    suspend fun fetchOne(armorId: String): Armor? {
        return withContext(Dispatchers.IO) {
            fetchOneInternal(armorId)
        }
    }

    private suspend fun fetchOneInternal(armorId: String): Armor? = suspendCoroutine {
        val db = FirebaseFirestore.getInstance()
        val result = mutableListOf<Armor>()
        db.collection(COLLECTION_PATH)
            .whereEqualTo(QUERY, armorId)
            .get()
            .addOnSuccessListener { snapShot ->
                for (document in snapShot) {
                    result.add(mapToArmor(document.data))
                }
                it.resume(result[0])
            }.addOnFailureListener { exception ->
                it.resumeWithException(exception)
            }
    }

    companion object {
        private const val COLLECTION_PATH = "ArmorCollection"
        private const val QUERY = "armorId"
    }
}