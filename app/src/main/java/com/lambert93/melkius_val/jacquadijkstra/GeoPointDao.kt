package com.lambert93.melkius_val.jacquadijkstra

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query

@Dao
interface GeoPointDao {
    @Query("SELECT GEO_POI_ID, GEO_POI_LATITUDE, GEO_POI_LONGITUDE, GEO_POI_NOM, GEO_POI_PARTITION FROM GEO_POINT")
    fun getAll(): List<GEO_POINT>
}