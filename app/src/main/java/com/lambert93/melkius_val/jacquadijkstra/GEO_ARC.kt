package com.lambert93.melkius_val.jacquadijkstra

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "GEO_ARC")
data class GEO_ARC(
    @PrimaryKey
    @ColumnInfo(name = "GEO_ARC_ID") var id: Int,
    @ColumnInfo(name = "GEO_ARC_DEB") var deb: Int,
    @ColumnInfo(name = "GEO_ARC_FIN") var fin: Int,
    @ColumnInfo(name = "GEO_ARC_TEMPS") var temps: Float,
    @ColumnInfo(name = "GEO_ARC_DISTANCE") var distance: Float,
    @ColumnInfo(name = "GEO_ARC_SENS") var sens: Int
)
