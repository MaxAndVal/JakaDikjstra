package com.lambert93.melkius_val.jacquadijkstra

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.huma.room_for_asset.RoomAsset


@Database(entities = [GEO_ARC::class], version = 1, exportSchema = false)
abstract class SIG_DataBase : RoomDatabase() {

    abstract fun GeoArcDao(): GeoArcDao

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

    }

}