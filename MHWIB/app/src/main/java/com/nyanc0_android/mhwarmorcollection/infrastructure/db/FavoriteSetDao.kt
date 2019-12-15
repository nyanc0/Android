package com.nyanc0_android.mhwarmorcollection.infrastructure.db

import androidx.room.*

@Dao
interface FavoriteSetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteSet(favoriteSetEntity: FavoriteSetEntity)

    @Update
    fun updateFavoriteSet(favoriteSetEntity: FavoriteSetEntity)

    @Delete
    fun delete(favoriteSetEntity: FavoriteSetEntity)

    @Query("select * from favoritesetentity")
    fun findAll(): List<FavoriteSetEntity>

    @Query("select * from favoritesetentity where setName = :setName")
    fun findByName(setName: String): List<FavoriteSetEntity>
}