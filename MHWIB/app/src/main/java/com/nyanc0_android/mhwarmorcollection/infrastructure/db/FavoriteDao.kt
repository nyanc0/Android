package com.nyanc0_android.mhwarmorcollection.infrastructure.db

import androidx.room.*

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(favorite: Favorite)

    @Update
    fun updateFavorite(favorite: Favorite)

    @Delete
    fun delete(favorite: Favorite)

    @Query("select * from favorite")
    fun findAll(): List<Favorite>?

    @Query("select * from favorite where armorId = :id")
    fun find(id: String): Favorite?
}