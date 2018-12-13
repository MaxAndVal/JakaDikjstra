package com.lambert93.melkius_val.jacquadijkstra

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_etendue2.*
import android.widget.Toast
import com.google.common.io.Flushables.flush
import android.R.attr.x
import android.content.Context
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import android.content.Context.MODE_PRIVATE
import android.os.Environment
import android.os.Environment.getExternalStorageDirectory
import org.xmlpull.v1.XmlSerializer
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


const val TAG = "TAG_DEBUG"

class ActivityEtendue2 : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    lateinit var db: SIG_DataBase
    private var listGeoArc: List<GEO_ARC> = ArrayList<GEO_ARC>()
    private var listGeoPoint: List<GEO_POINT> = ArrayList<GEO_POINT>()
    private var spinnerItems = ArrayList<String>()
    private lateinit var xmlSerializer: XmlSerializer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_etendue2)

        db = SIG_DataBase.getSIGDataBase(this)!!

        listGeoArc = getAllArc(db)
        listGeoPoint = getAllVertex(db)

        Log.d(TAG, "geopoints : $listGeoPoint")
        Log.d(TAG, "geoarcs: $listGeoArc")

        for ((itemPos, item) in listGeoPoint.withIndex()) {
            spinnerItems.add(itemPos, "Ligne ${item.partition} : ${item.nom}")
        }

        spinner_start.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerItems)
        spinner_end.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerItems)

        btn_dj.setOnClickListener { execDijkstra(Graph(listGeoPoint, listGeoArc)) }
    }

    private fun execDijkstra(graph: Graph) {
        val dijkstra = DijkstraAlgorithm(graph)
        dijkstra.execute(graph.nodes.find { "Ligne ${it.partition} : ${it.nom}" == spinner_start.selectedItem }!!)
        var path =
            dijkstra.getPath(graph.nodes.find { "Ligne ${it.partition} : ${it.nom}" == spinner_end.selectedItem }!!)

        if (path == null) {
            dijkstra.execute(graph.nodes.find { "Ligne ${it.partition} : ${it.nom}" == spinner_end.selectedItem }!!)
            path =
                    dijkstra.getPath(graph.nodes.find { "Ligne ${it.partition} : ${it.nom}" == spinner_start.selectedItem }!!)
            path?.reverse()
        }

        var kmlArgs: Array<String> = emptyArray()
        var stringPath = ""
        if (path != null) {
            for (spot in path!!) {
                kmlArgs += spot.nom
                stringPath += " -> "
                stringPath += spot.nom
            }
            Log.d(TAG, "kmlArgs: ${kmlArgs[1]}")
        }


        if (stringPath == "") stringPath = getString(R.string.no_itinerary)
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

    private fun generateKML(args: Array<String>) {

        val root = filesDir

        var fileos: FileOutputStream? = null
        val sb = StringBuilder()
        try {
            val rootu = Environment.getExternalStorageDirectory().toString()
            val myDir = File(rootu + "/rutas")
            myDir.mkdirs()
            var file = File(myDir, "Ruta.kml")

            /*The filename for me it's always going to be the same since
                           * I'm going to upload it to my server and my server handles
                           * the saving and storage*/

            fileos = openFileOutput("Ruta.kml", Context.MODE_PRIVATE)

        } catch (e: FileNotFoundException) {
            // Log.e("FileNotFoundException", e.toString());
            e.printStackTrace()
            Toast.makeText(this@ActivityEtendue2, "Error en outputstream", Toast.LENGTH_SHORT).show()

        }


        // Here begins the KML creation
        try {

            xmlSerializer = XmlPullParserFactory.newInstance().newSerializer()
        } catch (e1: XmlPullParserException) {
            // TODO Auto-generated catch block
            e1.printStackTrace()
            Toast.makeText(this@ActivityEtendue2, "Error en serializer", Toast.LENGTH_SHORT).show()
        }


        try {

            /* This KML file has a really basic structure, if you need
                           * styles and other fancy stuff, you are always free to add
                           * those style lines or anything you might need in order to
                           * make it look goof :) */

            xmlSerializer.setOutput(fileos, "UTF-8")
            xmlSerializer.startDocument(null, null)
            xmlSerializer.setFeature(
                "http://xmlpull.org/v1/doc/features.html#indent-output",
                true
            )
            xmlSerializer.startTag(null, "kml")

            xmlSerializer.startTag(null, "Document")

            xmlSerializer.startTag(null, "name")
            xmlSerializer.text("Ruta de Usuario ")
            xmlSerializer.endTag(null, "name")

            xmlSerializer.startTag(null, "description")
            xmlSerializer.cdsect("Ruta generada por: Usuario")
            xmlSerializer.endTag(null, "description")

            xmlSerializer.startTag(null, "Placemark")
            xmlSerializer.startTag(null, "name")
            xmlSerializer.text("Inicio")
            xmlSerializer.endTag(null, "name")
            xmlSerializer.startTag(null, "description")
            xmlSerializer.cdsect("")
            xmlSerializer.endTag(null, "description")
            xmlSerializer.startTag(null, "Point")
            xmlSerializer.startTag(null, "coordinates")

            xmlSerializer.text(
                java.lang.Double.toString(lons.get(0) as Double) + ","
                        + java.lang.Double.toString(lats.get(0) as Double) + ",0.000000"
            )
            xmlSerializer.endTag(null, "coordinates")
            xmlSerializer.endTag(null, "Point")
            xmlSerializer.endTag(null, "Placemark")

            // Poligons

            xmlSerializer.startTag(null, "Placemark")
            xmlSerializer.startTag(null, "name")
            xmlSerializer.text("Ruta del Usuario ")
            xmlSerializer.endTag(null, "name")
            xmlSerializer.startTag(null, "description")
            xmlSerializer.cdsect("")
            xmlSerializer.endTag(null, "description")

            xmlSerializer.startTag(null, "LineString")
            xmlSerializer.startTag(null, "tessellate")
            xmlSerializer.text("1")
            xmlSerializer.endTag(null, "tessellate")
            xmlSerializer.startTag(null, "coordinates")
            sb.setLength(0)

            /*This is the part where I get the size of the path arraylist
                           * and start printing the coordinates for my path*/

            val size = coorlo.size()
            x = 0
            while (x < size) {

                sb.append(java.lang.Double.toString(coorlo.get(x) as Double))
                sb.append(",")
                sb.append(java.lang.Double.toString(coorla.get(x) as Double))
                sb.append(",0\n")
                x++
            }

            sb.setLength(sb.length - 2)
            val s = sb.toString()
            xmlSerializer.text(s)
            xmlSerializer.endTag(null, "coordinates")
            xmlSerializer.endTag(null, "LineString")
            xmlSerializer.endTag(null, "Placemark")

            xmlSerializer.startTag(null, "Placemark")
            xmlSerializer.startTag(null, "name")
            xmlSerializer.text("Fin")
            xmlSerializer.endTag(null, "name")
            xmlSerializer.startTag(null, "description")
            xmlSerializer.cdsect("")
            xmlSerializer.endTag(null, "description")
            xmlSerializer.startTag(null, "Point")
            xmlSerializer.startTag(null, "coordinates")

            xmlSerializer.text(
                (java.lang.Double.toString(lonfi.get(0) as Double) + ","
                        + java.lang.Double.toString(latfi.get(0) as Double) + ",0.000000")
            )
            xmlSerializer.endTag(null, "coordinates")
            xmlSerializer.endTag(null, "Point")
            xmlSerializer.endTag(null, "Placemark")

            xmlSerializer.endTag(null, "Document")
            xmlSerializer.endTag(null, "kml")
            xmlSerializer.endDocument()
            xmlSerializer.flush()
            fileos!!.close()

            /* Once all is done and the file is generated I call my upload function to upload the file
                       * into my webserver. PS: You should use an ASYNCTASK to do things like this, I'm gonna
                       * correct this thing later*/

            //val absolutePath = baseContext.getFileStreamPath("Ruta.kml").toString()
            //uploadFile(absolutePath)


        } catch (e: IOException) {
            e.printStackTrace()
            // Log.e("Exception", "Exception occured in wroting");
            Toast.makeText(this@ActivityEtendue2, "Error en la generacion del archivo", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
