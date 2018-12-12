package com.lambert93.melkius_val.jacquadijkstra

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_etendue2.*

class ActivityEtendue2 : AppCompatActivity(){


    private var nodes: MutableList<Vertex>? = null
    private var edges: MutableList<Edge>? = null
    lateinit var db: SIG_DataBase
    private var listGeoArc: List<GEO_ARC> = ArrayList<GEO_ARC>()
    private var listGeoPoint: List<GEO_POINT> = ArrayList<GEO_POINT>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_etendue2)

        db = SIG_DataBase.getSIGDataBase(this)!!

        listGeoArc = getAllArc(db)
        listGeoPoint = getAllVertex(db)

        var listArcLine1: MutableList<GEO_ARC> = ArrayList()
        for (i in 184..210) {
            listArcLine1.add(listGeoArc[i])
        }
        var listPointLine1: MutableList<GEO_POINT> = ArrayList()
        for (i in 0..27) {
            listPointLine1.add(listGeoPoint[i])
        }

        Log.d("VertexDBSize", "arc size = ${listGeoArc}")
        Log.d("VertexDBSize", "vertex size = ${listGeoPoint}")


        // Lets check from location Loc_1 to Loc_10
        val graph = Graph(listGeoPoint, listGeoArc)
        val dijkstra = DijkstraAlgorithm(graph)
        dijkstra.execute(listPointLine1[0])
        val path = dijkstra.getPath(listPointLine1[27])


        /*for (vertex in path!!) {
            Log.d("jqfhqjshf", vertex.toString())
            hello.text = String.format("%s / %s", hello.text, vertex.toString())
        }*/

        hello.text = path.toString()
    }

    private fun getAllArc(db: SIG_DataBase): List<GEO_ARC> {
        Log.d("Function", "getAllarc")
        return db.GeoArcDao().getAll()
    }

    private fun getAllVertex(db: SIG_DataBase): List<GEO_POINT> {
        Log.d("Function", "getAllarc")
        return db.GeoPointDao().getAll()
    }
}
