package com.lambert93.melkius_val.jacquadijkstra

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query

@Dao
interface GeoArcDao {
    @Query("SELECT * FROM GEO_ARC")
    fun getAll(): List<GEO_ARC>
}