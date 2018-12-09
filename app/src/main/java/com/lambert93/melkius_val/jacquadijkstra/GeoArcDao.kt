package com.lambert93.melkius_val.jacquadijkstra

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query

@Dao
interface GeoArcDao {
    @Query("SELECT GEO_ARC_ID, GEO_ARC_DEB, GEO_ARC_FIN, CAST(GEO_ARC_TEMPS as Double), CAST(GEO_ARC_DISTANCE as Double), GEO_ARC_SENS FROM GEO_ARC")
    fun getAll(): List<GEO_ARC>
}