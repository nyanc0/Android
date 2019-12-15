package com.nyanc0_android.mhwarmorcollection.infrastructure.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.nyanc0_android.mhwarmorcollection.infrastructure.mapper.mapToArmor
import com.nyanc0_android.mhwarmorcollection.infrastructure.model.Armor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ArmorNameRepository {

    suspend fun fetchOne(armorName: String): Armor {
        return withContext(Dispatchers.IO) {
            fetchOneInternal(armorName)
        }
    }

    suspend fun fetch(armorNames: List<String>): List<Armor> {
        return armorNames.map {
            fetchOne(it)
        }
    }

    suspend fun fetch(armorName: String): List<Armor> {
        return withContext(Dispatchers.IO) {
            fetchInternal(armorName)
        }
    }

    suspend fun fetchRecord(armorNames: List<String>): List<Armor> {
        return armorNames.filter {
            it.length > 1
        }.mapNotNull {
            fetchOneAbout(it)
        }
    }

    private suspend fun fetchOneInternal(armorName: String): Armor = suspendCoroutine {
        val result = mutableListOf<Armor>()
        val db = FirebaseFirestore.getInstance()
        db.collection(COLLECTION_PATH)
            .whereEqualTo(QUERY, armorName)
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

    suspend fun fetchOneAbout(armorName: String): Armor? {
        return withContext(Dispatchers.IO) {
            fetchOneInternalAbout(armorName)
        }
    }

    private suspend fun fetchOneInternalAbout(armorName: String): Armor? = suspendCoroutine {
        val result = mutableListOf<Armor>()
        val db = FirebaseFirestore.getInstance()
        db.collection(COLLECTION_PATH)
            .whereEqualTo(QUERY, armorName)
            .get()
            .addOnSuccessListener { snapShot ->
                for (document in snapShot) {
                    result.add(mapToArmor(document.data))
                }
                if (result.isNotEmpty()) {
                    it.resume(result[0])
                } else {
                    it.resume(null)
                }
            }.addOnFailureListener { exception ->
                it.resumeWithException(exception)
            }
    }

    private suspend fun fetchInternal(armorName: String): List<Armor> = suspendCoroutine {
        val result = mutableListOf<Armor>()
        val db = FirebaseFirestore.getInstance()
        db.collection(COLLECTION_PATH)
            .whereLessThanOrEqualTo(QUERY, armorName)
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
        private const val QUERY = "armorName"
    }
}