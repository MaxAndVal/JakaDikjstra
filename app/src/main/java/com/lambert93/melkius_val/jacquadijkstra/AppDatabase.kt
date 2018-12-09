package com.lambert93.melkius_val.jacquadijkstra

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.huma.room_for_asset.RoomAsset


@Database(entities = [GEO_ARC::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun GeoArcDao(): GeoArcDao

    companion object {

        private var INSTANCE: AppDatabase? = null

        fun getAppDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                INSTANCE = RoomAsset.databaseBuilder(context, AppDatabase::class.java, "sqlite.db")
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }

}