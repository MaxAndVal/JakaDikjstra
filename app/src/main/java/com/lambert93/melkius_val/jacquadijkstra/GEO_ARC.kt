package com.lambert93.melkius_val.jacquadijkstra

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName= "GEO_ARC")
data class GEO_ARC(
    @PrimaryKey(autoGenerate = true) var GEO_ARC_ID: Int?,
    @ColumnInfo(name = "GEO_ARC_DEB") var GEO_ARC_DEB: Int?,
    @ColumnInfo(name = "GEO_ARC_FIN") var GEO_ARC_FIN: Int?,
    @ColumnInfo(name = "GEO_ARC_TEMPS") var GEO_ARC_TEMPS: Double?,
    @ColumnInfo(name = "GEO_ARC_DISTANCE") var GEO_ARC_DISTANCE: Double?,
    @ColumnInfo(name = "GEO_ARC_SENS") var GEO_ARC_SENS: Int?
)

