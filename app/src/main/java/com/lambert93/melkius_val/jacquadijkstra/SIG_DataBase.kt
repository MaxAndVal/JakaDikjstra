package com.lambert93.melkius_val.jacquadijkstra

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import android.content.Context
import com.huma.room_for_asset.RoomAsset


@Database(entities = [GEO_ARC::class, GEO_POINT::class], version = 2,  exportSchema = false)
abstract class SIG_DataBase : RoomDatabase() {

    abstract fun GeoArcDao(): GeoArcDao
    abstract fun GeoPointDao(): GeoPointDao

    companion object {

        private var INSTANCE: SIG_DataBase? = null

        fun getSIGDataBase(context: Context): SIG_DataBase? {
            if (INSTANCE == null) {
                INSTANCE = RoomAsset.databaseBuilder(context, SIG_DataBase::class.java, "lp_iem_sig.db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }

        val MIGRATION_1_3 = object : Migration(1, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {

            }
        }
    }

}