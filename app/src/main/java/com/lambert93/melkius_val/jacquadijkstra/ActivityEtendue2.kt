package com.lambert93.melkius_val.jacquadijkstra

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_etendue2.*

class ActivityEtendue2 : AppCompatActivity() {

    private var nodes: MutableList<Vertex>? = null
    private var edges: MutableList<Edge>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_etendue2)

        nodes = ArrayList()
        edges = ArrayList()
        for (i in 0..10) {
            val location = Vertex("Node_$i", "Node_$i")
            (nodes as ArrayList<Vertex>).add(location)
        }

        addLane("Edge_0", 0, 1, 85)
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
        addLane("Edge_11", 1, 10, 600)

        // Lets check from location Loc_1 to Loc_10
        val graph = Graph(nodes as ArrayList<Vertex>, edges as ArrayList<Edge>)
        val dijkstra = DijkstraAlgorithm(graph)
        dijkstra.execute((nodes as ArrayList<Vertex>).get(0))
        val path = dijkstra.getPath((nodes as ArrayList<Vertex>)[10])


        for (vertex in path!!) {
            Log.d("jqfhqjshf", vertex.toString())
            hello.text = String.format("%s / %s", hello.text, vertex.toString())
        }

    }

    private fun addLane(
        laneId: String, sourceLocNo: Int, destLocNo: Int,
        duration: Int
    ) {
        val lane = Edge(laneId, nodes!!.get(sourceLocNo), nodes!![destLocNo], duration)
        edges?.add(lane)
    }
}
