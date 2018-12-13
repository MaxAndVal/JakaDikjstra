package com.lambert93.melkius_val.jacquadijkstra

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_etendue2.*
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.PrintWriter
import java.util.*

const val TAG = "TAG_DEBUG"


//@SuppressLint("ByteOrderMark")
class ActivityEtendue2 : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    companion object {
        const val REQUEST_PERMISSION = 1
    }

    lateinit var db: SIG_DataBase
    private var listGeoArc: List<GEO_ARC> = ArrayList()
    private var listGeoPoint: List<GEO_POINT> = ArrayList()
    private var spinnerItems = ArrayList<String>()
    private var itinerary = ""
    private var itineraryName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_etendue2)

        db = SIG_DataBase.getSIGDataBase(this)!!

        listGeoArc = getAllArc(db)
        listGeoPoint = getAllVertex(db)

        Log.d(TAG, "GeoPoints : $listGeoPoint")
        Log.d(TAG, "GeoArcs: $listGeoArc")

        for ((itemPos, item) in listGeoPoint.withIndex()) {
            spinnerItems.add(itemPos, "Ligne ${item.partition} : ${item.nom}")
        }

        spinner_start.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerItems)
        spinner_end.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerItems)

        btn_dj.setOnClickListener { execDijkstra(Graph(listGeoPoint, listGeoArc)) }

        btn_kml.setOnClickListener { kmlGenerationWithPermission() }

    }

    private fun kmlGenerationWithPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_PERMISSION
            )
        } else {
            createKMLFile()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createKMLFile()
            }
        }
    }

    private fun execDijkstra(graph: Graph) {
        val dijkstra = DijkstraAlgorithm(graph)
        dijkstra.execute(graph.nodes.find { "Ligne ${it.partition} : ${it.nom}" == spinner_start.selectedItem }!!)
        val path =
            dijkstra.getPath(graph.nodes.find { "Ligne ${it.partition} : ${it.nom}" == spinner_end.selectedItem }!!)

        var kmlArgs: Array<String> = emptyArray()
        var stringPath = ""
        if (path != null) {
            for (spot in path) {
                kmlArgs += spot.nom
                stringPath += " -> "
                stringPath += spot.nom
            }
            Log.d(TAG, "kmlArgs: ${kmlArgs[1]}")
            btn_kml.isEnabled = true
            btn_kml.setTextColor(Color.parseColor("#ff0099cc"))
            generateKML(path)
        }

        if (stringPath == "") {
            stringPath = getString(R.string.no_itinerary)
            btn_kml.isEnabled = false
            btn_kml.setTextColor(Color.GRAY)
        }

        trajet.text = stringPath
    }

    private fun getAllArc(db: SIG_DataBase): List<GEO_ARC> {
        return db.GeoArcDao().getAll()
    }

    private fun getAllVertex(db: SIG_DataBase): List<GEO_POINT> {
        return db.GeoPointDao().getAll()
    }

    private fun generateKML(args: LinkedList<GEO_POINT>) {

        val sb = StringBuilder()

        sb.append("<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<kml xmlns='http://www.opengis.net/kml/2.2'>\n" +
                "\t\t<Document>\n" +
                "\t\t\t<name>Mon parcours</name>\n" +
                "\t\t\t<description>Itineraire de ${args[0].nom} ligne ${args[0].partition} vers ${args[args.size-1].nom} ligne ${args[args.size-1].partition}</description>\n" +
                "\t\t\t<Style id='LineGreenPoly'>\n" +
                "\t\t\t\t<LineStyle>\n" +
                "\t\t\t\t\t<color>7c4d10</color>\n" +
                "\t\t\t\t\t<width>8</width>\n" +
                "\t\t\t\t</LineStyle>\n" +
                "\t\t\t\t<PolyStyle>\n" +
                "\t\t\t\t\t<color>004678</color>\n" +
                "\t\t\t\t</PolyStyle>\n" +
                "\t\t\t</Style>\n" +
                "\t\t\t<Placemark>\n" +
                "\t\t\t\t<name>Mon parcours</name>\n" +
                "\t\t\t\t<description>Itineraire de ${args[0].nom} ligne ${args[0].partition} vers ${args[args.size-1].nom} ligne ${args[args.size-1].partition}</description>\n" +
                "\t\t\t\t<styleUrl>#LineGreenPoly</styleUrl>\n" +
                "\t\t\t\t<LineString>\n" +
                "\t\t\t\t\t<extrude>1</extrude>\n" +
                "\t\t\t\t\t<tessellate>1</tessellate>\n" +
                "\t\t\t\t\t<altitudeMode>clampToGround</altitudeMode>\n" +
                "\t\t\t\t\t<coordinates>\n")
                for (arg in args) {
                    sb.append("\t\t\t\t\t\t${arg.longitude},${arg.latitude}7\n")
                }
                sb.append("\t\t\t\t\t</coordinates>\n" +
                        "\t\t\t\t</LineString>\n" +
                        "\t\t\t</Placemark>\n" +
                        "\t\t</Document>\n" +
                        "</kml>")

        Log.d(TAG,"KML FILE : \n $sb")

        itineraryName = "${args[0].nom}_${args[args.size-1].nom}"
        itinerary = sb.toString()
    }

    private fun createKMLFile() {
        try {
            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "$itineraryName.kml")
            val fileWriter = FileWriter(file)
            val printWriter = PrintWriter(fileWriter);
            printWriter.print(itinerary)
            printWriter.close()

            Log.d(TAG, "createKMLFile ok")
            Toast.makeText(this, "Fichier KML créée dans le dossier Download", Toast.LENGTH_LONG).show()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.geoportail.gouv.fr/carte"))
            startActivity(intent)
        } catch (e: IOException) {
            Toast.makeText(this, "Une erreur est surnevue lors de la création du fichier", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "createKMLFile error")
            throw e.fillInStackTrace()
        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
