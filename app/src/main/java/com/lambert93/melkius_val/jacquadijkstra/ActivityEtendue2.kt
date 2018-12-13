package com.lambert93.melkius_val.jacquadijkstra

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_etendue2.*

class ActivityEtendue2 : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    lateinit var db: SIG_DataBase
    private var listGeoArc: List<GEO_ARC> = ArrayList<GEO_ARC>()
    private var listGeoPoint: List<GEO_POINT> = ArrayList<GEO_POINT>()
    private var spinnerItems = ArrayList<String>()

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

        Log.d("VertexDBSize", "arc size = $listGeoArc")
        Log.d("VertexDBSize", "vertex size = $listGeoPoint")

        for ((itemPos, item) in listGeoPoint.withIndex()) {
            spinnerItems.add(itemPos, item.nom)
        }

        spinner_start.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerItems)
        spinner_end.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerItems)

        btn_dj.setOnClickListener { execDijkstra(Graph(listGeoPoint, listGeoArc)) }
    }

    private fun execDijkstra(graph: Graph) {
        val dijkstra = DijkstraAlgorithm(graph)
        dijkstra.execute(graph.vertexes.find { it.nom == spinner_start.selectedItem }!!)
        var path = dijkstra.getPath(graph.vertexes.find { it.nom == spinner_end.selectedItem }!!)

        if (path == null) {
            dijkstra.execute(graph.vertexes.find { it.nom == spinner_end.selectedItem }!!)
            path = dijkstra.getPath(graph.vertexes.find { it.nom == spinner_start.selectedItem }!!)
            path?.reverse()
        }

        var stringPath = ""
        if (path != null) {
            for (spot in path!!) {
                stringPath+=" -> "
                stringPath+=spot.nom
            }
        }

        if (stringPath == "") stringPath = "Aucun itinéraire trouvé entre les deux destinations"
        hello.text = stringPath
    }

    private fun getAllArc(db: SIG_DataBase): List<GEO_ARC> {
        Log.d("Function", "getAllarc")
        return db.GeoArcDao().getAll()
    }

    private fun getAllVertex(db: SIG_DataBase): List<GEO_POINT> {
        Log.d("Function", "getAllarc")
        return db.GeoPointDao().getAll()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
