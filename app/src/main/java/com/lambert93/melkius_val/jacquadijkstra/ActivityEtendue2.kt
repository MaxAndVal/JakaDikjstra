package com.lambert93.melkius_val.jacquadijkstra

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_etendue2.*

class ActivityEtendue2 : AppCompatActivity(){


    private var nodes: MutableList<Vertex>? = null
    private var edges: MutableList<Edge>? = null
    lateinit var db: SIG_DataBase
    var listGeoArc: List<GEO_ARC> = ArrayList()
    var listGeoPointDao: List<GEO_POINT> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_etendue2)

        db = SIG_DataBase.getSIGDataBase(this)!!

        listGeoArc = getAllArc(db)
        listGeoPointDao = getAllVertex(db)


        Log.d("madatabase : ", "$listGeoArc")
        Log.d("VertexDBSize", "arc size = ${listGeoArc}")
        Log.d("VertexDBSize", "vertex size = ${listGeoPointDao}")

        nodes = ArrayList()
        edges = ArrayList()
        for (i in 0..10) {
            val location = Vertex("Node$i", "Name$i")
            Log.d("VertexTag", "$location")
            (nodes as ArrayList<Vertex>).add(location)
        }


        /*addLane("Edge_0", 0, 1, 85)
        addLane("Edge_1", 0, 2, 217)
        addLane("Edge_2", 0, 4, 173)
        addLane("Edge_3", 2, 6, 186)
        addLane("Edge_4", 2, 7, 103)
        addLane("Edge_5", 3, 7, 183)
        addLane("Edge_6", 5, 8, 250)
        addLane("Edge_7", 8, 9, 84)
        addLane("Edge_8", 7, 9, 167)
        addLane("Edge_9", 4, 9, 502)
        addLane("Edge_10", 9, 10, 40)
        addLane("Edge_11", 1, 10, 600)*/

        // Lets check from location Loc_1 to Loc_10
        val graph = Graph(listGeoPointDao, listGeoArc)
        val dijkstra = DijkstraAlgorithm(graph)
        dijkstra.execute(listGeoPointDao[0])
        val path = dijkstra.getPath(listGeoPointDao[28])


        /*for (vertex in path!!) {
            Log.d("jqfhqjshf", vertex.toString())
            hello.text = String.format("%s / %s", hello.text, vertex.toString())
        }*/

        hello.text = path.toString()
    }

    private fun addLane(laneId: String, sourceLocNo: Int, destLocNo: Int, duration: Int) {
        val lane = Edge(laneId, nodes!!.findLast { it.id as Int == sourceLocNo }!!, nodes!!.find { it.id as Int == destLocNo }!!, duration)
        edges?.add(lane)
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
