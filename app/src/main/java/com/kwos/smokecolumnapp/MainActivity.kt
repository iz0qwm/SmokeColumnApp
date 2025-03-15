package com.kwos.smokecolumnapp

import com.kwos.smokecolumnapp.ui.theme.DrawView
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.*
import com.google.android.gms.maps.model.LatLng
import android.widget.ArrayAdapter
import android.app.Activity
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.exifinterface.media.ExifInterface
import android.graphics.PointF
import android.view.MotionEvent
import android.widget.Toast
import kotlin.math.sqrt
import android.view.GestureDetector
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import java.io.File
import java.io.IOException
import java.io.ByteArrayInputStream
import java.util.regex.Pattern
import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.DocumentBuilder
import org.xml.sax.InputSource
import org.apache.tika.parser.AutoDetectParser
import org.apache.tika.parser.ParseContext
import java.io.FileInputStream
import org.apache.tika.metadata.Metadata
import org.w3c.dom.Document
//import org.jdom2.Document
import org.jdom2.input.SAXBuilder
//import org.jdom2.input.stream.InputStreamReader
import org.jdom2.Element
import org.jdom2.Namespace
import com.github.chrisbanes.photoview.PhotoView




class MainActivity : AppCompatActivity() {

    private val TAG = "SmokeColumnApp"

    // Variabili EditText
    private lateinit var editLat1: EditText
    private lateinit var editLon1: EditText
    private lateinit var editHeading1: EditText
    private lateinit var editLat2: EditText
    private lateinit var editLon2: EditText
    private lateinit var editHeading2: EditText
    private lateinit var editw: EditText
    private lateinit var iconSelectImage1: ImageView
    private lateinit var iconSelectImage2: ImageView

    private lateinit var imagePreview: PhotoView
    private lateinit var drawView: DrawView
    private lateinit var gestureDetector: GestureDetector


    //Altre variabili
    private var latDrone1: Double = 0.0
    private var lonDrone1: Double = 0.0
    private var yawDrone1: Double = 0.0
    private var latDrone2: Double = 0.0
    private var lonDrone2: Double = 0.0
    private var yawDrone2: Double = 0.0
    private var latFumo: Double = 0.0
    private var lonFumo: Double = 0.0
    private var useTwoDrones: Boolean = false

    var startPoint: PointF? = null
    var endPoint: PointF? = null
    var selectedWidthPx: Float = 0f  // Lunghezza in pixel


    private lateinit var imagePickerLauncher1: ActivityResultLauncher<Intent>
    private lateinit var imagePickerLauncher2: ActivityResultLauncher<Intent>

    data class XMPData(val latitude: Double?, val longitude: Double?, val yawDegree: Double?)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        val spinnerFOV = findViewById<Spinner>(R.id.spinnerFOV)
        val editFOV = findViewById<EditText>(R.id.editFOV)

        val droneFOVs = mapOf(
            "AIR 2S - 22mm - FOV 88°" to 88.0,
            "AIR 3 - 70mm - FOV 35°" to 35.0,
            "AIR 3 - 24mm - FOV 82.5°" to 82.0,
            "AIR 3S - 24mm - FOV 84°" to 84.0,
            "AIR 3S - 70mm - FOV 35°" to 35.0,
            "Mavic 2 PRO - 24mm - FOV 83°" to 83.0,
            "Mavic 2 Zoom - 24mm - FOV 83°" to 83.0,
            "Mavic 2 Zoom - 48mm - FOV 48°" to 48.0,
            "Mavic 3 Pro - 24mm - FOV 84°" to 84.0,
            "Mavic 3T - 24mm - FOV 84°" to 84.0,
            "Mavic 3T - 70mm - FOV 35°" to 35.0,
            "MINI 3 Pro - 24mm - FOV 82.1°" to 82.1,
            "MINI 4 Pro - 24mm - FOV 82.1°" to 82.1,
            "MINI 3 - 24mm - FOV 82.1°" to 82.1,
            "MINI 2 - 24mm - FOV 83°" to 83.0,
            "Mini 4k - 24mm - FOV 83°" to 83.0,
            "EVO II - 25.6mm - FOV 79°" to 79.0,
            "NESSUNO di questi" to null
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, droneFOVs.keys.toList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFOV.adapter = adapter

        val editW = findViewById<EditText>(R.id.editW)
        val editWf = findViewById<EditText>(R.id.editWf)
        editw = findViewById(R.id.editw)
        editLat1 = findViewById(R.id.editLat1)
        editLon1 = findViewById(R.id.editLon1)
        editHeading1 = findViewById(R.id.editHeading1)
        editLat2 = findViewById(R.id.editLat2)
        editLon2 = findViewById(R.id.editLon2)
        editHeading2 = findViewById(R.id.editHeading2)
        //val drawView = findViewById<DrawView>(R.id.drawView)
        val btnCalcola = findViewById<Button>(R.id.btnCalcola)
        val btnShowMap = findViewById<Button>(R.id.btnShowMap)
        val textResult = findViewById<TextView>(R.id.textResult)
        val algorithmSwitch = findViewById<androidx.appcompat.widget.SwitchCompat>(R.id.algorithmSwitch)

        //Selezione su immagine
        imagePreview = findViewById(R.id.imagePreview)
        drawView = findViewById(R.id.drawView)

        val btnInfo = findViewById<Button>(R.id.btnInfo)
        btnInfo.setOnClickListener {
            val intent = Intent(this, InfoActivity::class.java)
            startActivity(intent)
        }

        //SELEZIONE IMMAGINI PER RECUPERO COORDINATE
        // Collega le View
        iconSelectImage1 = findViewById(R.id.iconSelectImage1)
        iconSelectImage2 = findViewById(R.id.iconSelectImage2)

        // Launcher per selezionare un'immagine
        imagePickerLauncher1 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleImageResult(result, isDrone1 = true)
        }

        imagePickerLauncher2 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleImageResult(result, isDrone1 = false)
        }

        // Click listeners per selezione immagine
        iconSelectImage1.setOnClickListener { openImagePicker(imagePickerLauncher1) }
        iconSelectImage2.setOnClickListener { openImagePicker(imagePickerLauncher2) }
        // FINE SELEZIONE IMMAGINI



        useTwoDrones = true
        algorithmSwitch.isChecked = useTwoDrones
        spinnerFOV.visibility = View.GONE
        editFOV.visibility = View.GONE
        editW.visibility = View.GONE
        editWf.visibility = View.GONE
        editw.visibility = View.GONE

        algorithmSwitch.setOnCheckedChangeListener { _, isChecked ->
            useTwoDrones = isChecked

            if (useTwoDrones) {
                spinnerFOV.visibility = View.GONE
                editFOV.visibility = View.GONE
                editW.visibility = View.GONE
                editWf.visibility = View.GONE
                editw.visibility = View.GONE

                editLat2.visibility = View.VISIBLE
                editLon2.visibility = View.VISIBLE
                editHeading2.visibility = View.VISIBLE
                iconSelectImage2.visibility = View.VISIBLE
            } else {
                spinnerFOV.visibility = View.VISIBLE
                editFOV.visibility = View.VISIBLE
                editW.visibility = View.VISIBLE
                editWf.visibility = View.VISIBLE
                editw.visibility = View.VISIBLE

                editLat2.visibility = View.GONE
                editLon2.visibility = View.GONE
                editHeading2.visibility = View.GONE
                iconSelectImage2.visibility = View.GONE
            }
        }

        spinnerFOV.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedDrone = parent.getItemAtPosition(position).toString()
                val fovValue = droneFOVs[selectedDrone]

                if (fovValue != null) {
                    editFOV.setText(fovValue.toString())
                    editFOV.visibility = View.GONE
                    editFOV.isEnabled = false
                } else {
                    editFOV.setText("")
                    editFOV.visibility = View.VISIBLE
                    editFOV.isEnabled = true
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Crea un GestureDetector per distinguere tra tap singolo e zoom
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
                logDebug(TAG, "gestureDetector: Single tap detected at x: ${event.x}, y: ${event.y}")
                handleTouch(event.x, event.y)
                return true
            }
        })

        imagePreview.setOnTouchListener { view, event ->
            // Logga il numero di dita
            logDebug(TAG, "TouchListener: Event action: ${event.action}, pointer count: ${event.pointerCount}")

            // Lascia che PhotoView gestisca tutti gli eventi touch, compreso il pinch-to-zoom
            if (imagePreview.onTouchEvent(event)) {
                return@setOnTouchListener true
            }

            // Se è un tocco singolo, usa il GestureDetector per gestire la selezione del punto
            if (event.pointerCount == 1) {
                logDebug(TAG, "TouchListener: Un solo dito rilevato, passo a GestureDetector")
                gestureDetector.onTouchEvent(event)
            }
            // Per evitare il warning di performClick()
            if (event.action == MotionEvent.ACTION_DOWN) {
                view.performClick()
            }
            true
        }


        btnCalcola.setOnClickListener {
            try {
                latDrone1 = editLat1.text.toString().toDouble()
                lonDrone1 = editLon1.text.toString().toDouble()
                val heading1 = editHeading1.text.toString().toDouble()

                if (useTwoDrones) {
                    latDrone2 = editLat2.text.toString().toDouble()
                    lonDrone2 = editLon2.text.toString().toDouble()
                    val heading2 = editHeading2.text.toString().toDouble()
                    runAlgorithmForTwoDrones(heading1, heading2, textResult)
                } else {
                    val fovh = editFOV.text.toString().toDouble()
                    val widthimage = editWf.text.toString().toDouble()
                    val widthapp = editw.text.toString().toDouble()
                    //val widthcolumn = editW.text.toString().toDouble()
                    // ✅ Usa il valore selezionato sulla foto
                    val widthcolumn = selectedWidthPx.toDouble()

                    if (widthcolumn > 0) {
                        val distancefumo = metodo1(fovh, widthcolumn, widthimage, widthapp)
                        val (latitutdefumo, longitudefumo) = calcolaCoordinateFumo(latDrone1, lonDrone1, distancefumo, heading1)
                        latFumo = latitutdefumo
                        lonFumo = longitudefumo
                        textResult.text = "Distanza fumo: ${"%.2f".format(distancefumo)} m\n" +
                                "Lat: ${"%.6f".format(latFumo)}, Lon: ${"%.6f".format(lonFumo)}"
                    } else {
                        Toast.makeText(this, "Seleziona la colonna di fumo!", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                textResult.text = "Errore: Controlla i dati inseriti"
            }
        }

        btnShowMap.setOnClickListener {
            openMap()
        }
    }

    private fun metodo1(fovh: Double, widthcolumn: Double, widthimage: Double, widthapp: Double): Double {
        val fovhrad = Math.toRadians(fovh)
        return ((widthcolumn/1.1) / (2 * tan(fovhrad / 2))) * (widthimage / (widthapp*0.95))
    }

    private fun runAlgorithmForTwoDrones(yaw1: Double, yaw2: Double, textResult: TextView) {
        try {
            val result = trovaIntersezione(latDrone1, lonDrone1, yaw1, latDrone2, lonDrone2, yaw2)
            latFumo = result.latitude
            lonFumo = result.longitude
            textResult.text = "Lat: ${"%.6f".format(latFumo)}, Lon: ${"%.6f".format(lonFumo)}"
        } catch (e: Exception) {
            textResult.text = "Errore: ${e.message}"
        }
    }

    private fun trovaIntersezione(lat1: Double, lon1: Double, yaw1: Double, lat2: Double, lon2: Double, yaw2: Double): LatLng {
        val distanzaFittizia = 20000.0
        val punto1 = calcolaCoordinateFumo(lat1, lon1, distanzaFittizia, yaw1)
        val punto2 = calcolaCoordinateFumo(lat2, lon2, distanzaFittizia, yaw2)
        val x1 = lon1
        val y1 = lat1
        val x2 = punto1.second
        val y2 = punto1.first
        val x3 = lon2
        val y3 = lat2
        val x4 = punto2.second
        val y4 = punto2.first
        val det = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4)
        if (abs(det) < 1e-6) throw Exception("Le linee di vista sono quasi parallele.")
        val xFumo = ((x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4)) / det
        val yFumo = ((x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)) / det
        return LatLng(yFumo, xFumo)
    }

    private fun calcolaCoordinateFumo(lat: Double, lon: Double, distanza: Double, heading: Double): Pair<Double, Double> {
        val headingRad = Math.toRadians(heading)
        val latFumo = lat + (distanza * cos(headingRad)) / 111320
        val lonFumo = lon + (distanza * sin(headingRad)) / (111320 * cos(Math.toRadians(lat)))
        return Pair(latFumo, lonFumo)
    }

    private fun openMap() {
        val intent = Intent(this, MapActivity::class.java).apply {
            putExtra("LAT_FUMO", latFumo)
            putExtra("LON_FUMO", lonFumo)
            putExtra("LAT_DRONE1", latDrone1)
            putExtra("LON_DRONE1", lonDrone1)
            putExtra("LAT_DRONE2", latDrone2)
            putExtra("LON_DRONE2", lonDrone2)
            putExtra("USE_TWO_DRONES", useTwoDrones)
        }
        startActivity(intent)
    }
    private fun openImagePicker(launcher: ActivityResultLauncher<Intent>) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        launcher.launch(intent)
    }


    private fun extractDroneDataFromXMP(imageUri: Uri, isDrone1: Boolean) {
        try {
            val inputStream = contentResolver.openInputStream(imageUri)
            if (inputStream != null) {
                val exif = ExifInterface(inputStream)

                // 🔍 Legge i metadati XMP
                val xmpData = exif.getAttribute(ExifInterface.TAG_XMP)
                if (xmpData != null) {
                    val xmpResult = parseXMPData(xmpData)
                    if (xmpResult != null) {
                        val lat = xmpResult.latitude
                        val lon = xmpResult.longitude
                        val yaw = xmpResult.yawDegree

                        // 🛰️ Inserisce i dati nei campi corretti
                        if (lat != null && lon != null && yaw != null) {
                            if (isDrone1) {
                                latDrone1 = lat
                                lonDrone1 = lon
                                yawDrone1 = yaw
                                editLat1.setText(lat.toString())
                                editLon1.setText(lon.toString())
                                editHeading1.setText(yaw.toString())
                            } else {
                                latDrone2 = lat
                                lonDrone2 = lon
                                yawDrone2 = yaw
                                editLat2.setText(lat.toString())
                                editLon2.setText(lon.toString())
                                editHeading2.setText(yaw.toString())
                            }
                            logDebug(TAG, "Dati trovati - Lat: $lat, Lon: $lon, Yaw: $yaw}")
                        } else {
                            logDebug(TAG, "Dati GPS non trovati nei metadati XMP")
                        }
                    } else {
                        logDebug(TAG, "Errore nel parsing dei metadati XMP")
                    }
                } else {
                    logDebug(TAG, "Metadati XMP non trovati nell'immagine")
                }

                inputStream.close()
            } else {
                logError(TAG, "Impossibile aprire il file per leggere i dati XMP.")
            }
        } catch (e: IOException) {
            logError(TAG, "Errore nella lettura dei dati XMP: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun parseXMPData(xmpData: String): XMPData? {
        return try {
            val factory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
            factory.isNamespaceAware = true
            val builder = factory.newDocumentBuilder()
            val doc: Document = builder.parse(InputSource(StringReader(xmpData)))

            val descriptions = doc.getElementsByTagName("rdf:Description")
            if (descriptions.length > 0) {
                val description = descriptions.item(0)

                val latitude = description.attributes.getNamedItem("drone-dji:GpsLatitude")?.nodeValue?.toDoubleOrNull()
                val longitude = description.attributes.getNamedItem("drone-dji:GpsLongitude")?.nodeValue?.toDoubleOrNull()
                val yawDegree = description.attributes.getNamedItem("drone-dji:FlightYawDegree")?.nodeValue?.toDoubleOrNull()

                if (latitude != null && longitude != null && yawDegree != null) {
                    XMPData(latitude, longitude, yawDegree)
                } else {
                    null
                }
            } else {
                null
            }
        } catch (e: Exception) {
            logError(TAG, "Errore nel parsing dei dati XMP", e)
            null
        }
    }

    private fun handleImageResult(result: ActivityResult, isDrone1: Boolean) {
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val imageUri: Uri? = result.data!!.data
            if (imageUri != null) {
                // ✅ Usa direttamente l'URI, senza convertire in percorso file
                extractDroneDataFromXMP(imageUri, isDrone1)
                // Verifica se useTwoDrones è false prima di caricare l'immagine
                if (!useTwoDrones) {
                    logDebug(TAG, "hanedleImageResult: Carico immagine in PhotoView: $imageUri")
                    findViewById<PhotoView>(R.id.imagePreview).setImageURI(imageUri) // 🔹 Carica immagine
                }
            } else {
                logDebug(TAG, "URI immagine nullo")
            }
        }
    }

    private fun handleTouch(x: Float, y: Float) {
        logDebug(TAG, "handleTouch selection: Point selected at x: $x, y: $y")
        if (startPoint == null) {
            startPoint = PointF(x, y)
            Toast.makeText(this, "Seleziona il secondo punto", Toast.LENGTH_SHORT).show()
        } else {
            endPoint = PointF(x, y)

            // Calcola la distanza in pixel
            val dx = endPoint!!.x - startPoint!!.x
            val dy = endPoint!!.y - startPoint!!.y
            selectedWidthPx = sqrt(dx * dx + dy * dy).toFloat()
            editw.setText(selectedWidthPx.toInt().toString())

            // Disegna la linea sopra l'immagine
            drawView.setPoints(startPoint!!.x, startPoint!!.y, endPoint!!.x, endPoint!!.y)

            Toast.makeText(this, "Lunghezza selezionata: ${selectedWidthPx.toInt()} px", Toast.LENGTH_SHORT).show()

            // Reset per una nuova selezione
            startPoint = null
            endPoint = null
        }
    }

    override fun onResume() {
        super.onResume()
        imagePreview.setOnClickListener { it.performClick() }
    }

}



