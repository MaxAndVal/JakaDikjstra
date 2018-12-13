package com.lambert93.melkius_val.jacquadijkstra

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_etendue2.*
import java.util.*


const val TAG = "TAG_DEBUG"

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

        var kmlArgs: Array<String> = emptyArray()
        var stringPath = ""
        if (path != null) {
            for (spot in path!!) {
                kmlArgs += spot.nom
                stringPath += " -> "
                stringPath += spot.nom
            }
            Log.d(TAG, "kmlArgs: ${kmlArgs[1]}")
            generateKML(path)
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

    private fun generateKML(args: LinkedList<GEO_POINT>) {


//File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "file.kml")
        var sb = StringBuilder()

        sb.append("<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<kml xmlns='http://www.opengis.net/kml/2.2'>\n" +
                "\t\t<Document>\n" +
                "\t\t\t<name>Mon parcours</name>\n" +
                "\t\t\t<description>Itinéraire de ${args[0].nom} ligne ${args[0].partition} vers ${args[args.size-1].nom} ligne ${args[args.size-1].partition}</description>\n" +
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
                "\t\t\t\t<description>Itinéraire de ${args[0].nom} ligne ${args[0].partition} vers ${args[args.size-1].nom} ligne ${args[args.size-1].partition}</description>\n" +
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

    }

    private fun permission() {

        /*
        private  void DemandeDePermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                {
                   explain();
                }
                else
                {
                    askForPermission();
                }
            }
        }
        else
        {
            Graphe graph = new Graphe(pointList, arcList);
            execDjisktra(graph);



            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"export.kml");
            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {

                if(path!= null){
                    fileWriter.write("<?xml version='1.0' encoding='UTF-8'?>\n");
                    fileWriter.write("<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n");
                    fileWriter.write("<Document>\n" + "<Folder>\n" + "<name>Arret de bus</name>\n");

                    for (GeoPoint point : path) {
                        fileWriter.write("<Placemark>\n");
                        fileWriter.write("<name>" +point.getGeo_poi_nom()+ "</name>\n");
                        fileWriter.write("<Point>\n" +
                                "<coordinates>"+ point.getGeo_poi_longitude() +"," + point.getGeo_poi_latitude()
                                +"</coordinates>\n" +
                                "</Point>\n");
                        fileWriter.write("</Placemark>\n");

                    }
                    fileWriter.write( "<Placemark>\n");
                    fileWriter.write( "<name>Itineraire</name>\n" + "<LineString>\n" + "<coordinates>\n");
                    for (GeoPoint point : path) {
                        fileWriter.write(point.getGeo_poi_longitude() +"," + point.getGeo_poi_latitude()+"\n");
                    }
                    fileWriter.write("</coordinates>\n" + "</LineString>\n"+"</Placemark>\n");
                    fileWriter.write("</Folder>\n" + "</Document>\n"+"</kml>");
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
        */
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
