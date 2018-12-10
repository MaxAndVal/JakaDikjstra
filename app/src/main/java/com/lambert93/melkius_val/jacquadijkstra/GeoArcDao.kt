package com.lambert93.melkius_val.jacquadijkstra

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query

@Dao
interface GeoArcDao {
    @Query("SELECT GEO_ARC_ID, GEO_ARC_DEB, GEO_ARC_FIN, GEO_ARC_TEMPS, GEO_ARC_DISTANCE, GEO_ARC_SENS FROM GEO_ARC")
    fun getAll(): List<GEO_ARC>
}