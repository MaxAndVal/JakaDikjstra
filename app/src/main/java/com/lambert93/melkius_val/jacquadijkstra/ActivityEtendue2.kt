package com.lambert93.melkius_val.jacquadijkstra

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_etendue2.*
import org.xmlpull.v1.XmlSerializer


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

//        if (path == null) {
//            dijkstra.execute(graph.nodes.find { "Ligne ${it.partition} : ${it.nom}" == spinner_end.selectedItem }!!)
//            path =
//                    dijkstra.getPath(graph.nodes.find { "Ligne ${it.partition} : ${it.nom}" == spinner_start.selectedItem }!!)
//            path?.reverse()
//        }

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

        var sb = StringBuilder()
        sb.append("<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n")
            .append("<Document>\n")
            .append("<name>KML Bourg-en-Bresse Bus</name>\n")
            .append("<open>1</open>\n")
            .append("<description>Current path between source and target bus spot selected</description>\n")
            .append("Style id=\"downArrowIcon\"\n<IconStyle>\n<Icon>\n<href>http://maps.google.com/mapfiles/kml/pal4/icon28.png</href>\n")
            .append("</Icon>\n</IconStyle>\n</Style>\n")
            .append("<Folder>\n<name>Placemarks</name>\n")//TODO change with name of the spot
            .append("<description> </description>\n")
            .append("<LookAt>\n")

        for (i in 0..15) {
            sb.append("<longitude>-122.0839597145766</longitude>\n") //TODO change with the coords
                .append("<latitude>37.42222904525232</latitude>\n")
                .append("<altitude>0</altitude>\n")
                .append("</LookAt>\n")
        }

        /*
        <kml xmlns="http://www.opengis.net/kml/2.2">
  <Document>
    <name>KML Samples</name>
    <open>1</open>
    <description>Unleash your creativity with the help of these examples!</description>
    <Style id="downArrowIcon">
      <IconStyle>
        <Icon>
          <href>http://maps.google.com/mapfiles/kml/pal4/icon28.png</href>
        </Icon>
      </IconStyle>
    </Style>

        <Folder>
      <name>Placemarks</name>
      <description>These are just some of the different kinds of placemarks with
        which you can mark your favorite places</description>
      <LookAt>
        <longitude>-122.0839597145766</longitude>
        <latitude>37.42222904525232</latitude>
        <altitude>0</altitude>
        <heading>-148.4122922628044</heading>
        <tilt>40.5575073395506</tilt>
        <range>500.6566641072245</range>
      </LookAt>
      <Placemark>
        <name>Simple placemark</name>
        <description>Attached to the ground. Intelligently places itself at the
          height of the underlying terrain.</description>
        <Point>
          <coordinates>-122.0822035425683,37.42228990140251,0</coordinates>
        </Point>
      </Placemark>

      <Placemark>
        <name>Line</name>
        <visibility>0</visibility>
        <description>Transparent purple line</description>
        <LookAt>
          <longitude>-112.2719329043177</longitude>
          <latitude>36.08890633450894</latitude>
          <altitude>0</altitude>
          <heading>-106.8161545998597</heading>
          <tilt>44.60763714063257</tilt>
          <range>2569.386744398339</range>
        </LookAt>
        <styleUrl>#transPurpleLineGreenPoly</styleUrl>
        <LineString>
          <tessellate>1</tessellate>
          <altitudeMode>absolute</altitudeMode>
          <coordinates> -112.265654928602,36.09447672602546,2357 </coordinates>
        </LineString>
      </Placemark>
    </Folder>

    </kml>
    </Document>
    */

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
