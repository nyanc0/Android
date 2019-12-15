package com.nyanc0_android.mhwarmorcollection.infrastructure.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.nyanc0_android.mhwarmorcollection.infrastructure.mapper.mapToMonster
import com.nyanc0_android.mhwarmorcollection.infrastructure.model.Monster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MonsterRepository {

    suspend fun fetch(): List<Monster> {
        return withContext(Dispatchers.IO) {
            fetchInternal()
        }
    }

    private suspend fun fetchInternal(): List<Monster> = suspendCoroutine {
        val db = FirebaseFirestore.getInstance()
        val result = mutableListOf<Monster>()
        db.collection(COLLECTION_PATH)
            .get()
            .addOnSuccessListener { snapShot ->
                for (document in snapShot) {
                    result.add(mapToMonster(document.data))
                }
                it.resume(result)
            }.addOnFailureListener { exception ->
                it.resumeWithException(exception)
            }
    }

    companion object {
        private const val COLLECTION_PATH = "MonsterCollection"
    }
}