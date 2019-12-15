package com.nyanc0_android.mhwarmorcollection.infrastructure.repository

import android.content.Context
import com.nyanc0_android.mhwarmorcollection.infrastructure.db.Favorite
import com.nyanc0_android.mhwarmorcollection.infrastructure.db.FavoriteDatabase
import com.nyanc0_android.mhwarmorcollection.infrastructure.db.FavoriteSetDatabse
import com.nyanc0_android.mhwarmorcollection.infrastructure.db.FavoriteSetEntity
import com.nyanc0_android.mhwarmorcollection.infrastructure.model.Armor
import com.nyanc0_android.mhwarmorcollection.infrastructure.model.FavoriteSet
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteRepository(private val context: Context) {
    suspend fun getFavorite(armorId: String): Favorite? {
        return withContext(Dispatchers.IO) {
            val db = FavoriteDatabase.getInstance(context)
            db?.favoriteDao()?.find(armorId)
        }
    }

    suspend fun getAllFavorites(): List<Favorite> {
        return withContext(Dispatchers.IO) {
            val db = FavoriteDatabase.getInstance(context)
            db?.favoriteDao()?.findAll() ?: emptyList()
        }
    }

    fun saveFavorite(armor: Armor) = GlobalScope.launch(Dispatchers.IO) {
        val db = FavoriteDatabase.getInstance(context)
        db?.favoriteDao()?.insertFavorite(
            Favorite(
                armorId = armor.id,
                armorName = armor.name,
                monsterName = armor.monsterName,
                parts = armor.parts
            )
        )
    }

    fun deleteFavorite(armor: Armor) = GlobalScope.launch(Dispatchers.IO) {
        val db = FavoriteDatabase.getInstance(context)
        db?.favoriteDao()?.delete(
            Favorite(
                armorId = armor.id,
                armorName = armor.name,
                monsterName = armor.monsterName,
                parts = armor.parts
            )
        )
    }

    fun saveFavoriteSet(setName: String, armor: List<Armor>) {
        val adapter = Moshi.Builder().build().adapter(FavoriteSet::class.java)
        val favorites = armor.map {
            com.nyanc0_android.mhwarmorcollection.infrastructure.model.Favorite(
                armorName = it.name,
                armorId = it.id,
                monsterName = it.monsterName,
                parts = it.parts
            )
        }

        val json = adapter.toJson(FavoriteSet(favorites))
        val db = FavoriteSetDatabse.getInstance(context)
        db?.favoriteSetDao()?.insertFavoriteSet(FavoriteSetEntity(setName = setName, json = json))
    }

    suspend fun getAllFavoriteSet(): List<FavoriteSetEntity> {
        return withContext(Dispatchers.IO) {
            val db = FavoriteSetDatabse.getInstance(context)
            db?.favoriteSetDao()?.findAll() ?: emptyList()
        }
    }
}