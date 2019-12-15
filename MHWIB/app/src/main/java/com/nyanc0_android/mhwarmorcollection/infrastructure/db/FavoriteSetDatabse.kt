package com.nyanc0_android.mhwarmorcollection.infrastructure.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteSetEntity::class], version = 1)
abstract class FavoriteSetDatabse : RoomDatabase() {
    abstract fun favoriteSetDao(): FavoriteSetDao

    companion object {
        private var INSTANCE: FavoriteSetDatabse? = null

        fun getInstance(context: Context): FavoriteSetDatabse? {
            if (INSTANCE == null) {
                synchronized(FavoriteSetDatabse::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FavoriteSetDatabse::class.java,
                        "FavoriteSetDatabase.db"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}