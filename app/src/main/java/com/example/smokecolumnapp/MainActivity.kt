package com.example.smokecolumnapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.*
import com.google.android.gms.maps.model.LatLng
import android.widget.ArrayAdapter


class MainActivity : AppCompatActivity() {

    private var latDrone1: Double = 0.0
    private var lonDrone1: Double = 0.0
    private var latDrone2: Double = 0.0
    private var lonDrone2: Double = 0.0
    private var latFumo: Double = 0.0
    private var lonFumo: Double = 0.0
    private var useTwoDrones: Boolean = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinnerFOV = findViewById<Spinner>(R.id.spinnerFOV)
        val editFOV = findViewById<EditText>(R.id.editFOV)

        val droneFOVs = mapOf(
            "AIR 2S - 22mm - FOV 88°" to 88.0,
            "AIR 3 - 70mm - FOV 35°" to 35.0,
            "AIR 3 - 24mm - FOV 82.5°" to 82.0,
            "AIR 3S - 24mm - FOV 84°" to 84.0,
            "AIR 3S - 24mm - FOV 35°" to 35.0,
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
        val editw = findViewById<EditText>(R.id.editw)
        val editLat1 = findViewById<EditText>(R.id.editLat1)
        val editLon1 = findViewById<EditText>(R.id.editLon1)
        val editHeading1 = findViewById<EditText>(R.id.editHeading1)
        val editLat2 = findViewById<EditText>(R.id.editLat2)
        val editLon2 = findViewById<EditText>(R.id.editLon2)
        val editHeading2 = findViewById<EditText>(R.id.editHeading2)
        val btnCalcola = findViewById<Button>(R.id.btnCalcola)
        val btnShowMap = findViewById<Button>(R.id.btnShowMap)
        val textResult = findViewById<TextView>(R.id.textResult)
        val algorithmSwitch = findViewById<androidx.appcompat.widget.SwitchCompat>(R.id.algorithmSwitch)
        val btnInfo = findViewById<Button>(R.id.btnInfo)
        btnInfo.setOnClickListener {
            val intent = Intent(this, InfoActivity::class.java)
            startActivity(intent)
        }

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
            } else {
                spinnerFOV.visibility = View.VISIBLE
                editFOV.visibility = View.VISIBLE
                editW.visibility = View.VISIBLE
                editWf.visibility = View.VISIBLE
                editw.visibility = View.VISIBLE

                editLat2.visibility = View.GONE
                editLon2.visibility = View.GONE
                editHeading2.visibility = View.GONE
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
                    val FOV_h = editFOV.text.toString().toDouble()
                    val W = editW.text.toString().toDouble()
                    val w_f = editWf.text.toString().toDouble()
                    val w = editw.text.toString().toDouble()
                    val D_fumo = metodo1(FOV_h, W, w_f, w)
                    val (lat_fumo, lon_fumo) = calcolaCoordinateFumo(latDrone1, lonDrone1, D_fumo, heading1)
                    latFumo = lat_fumo
                    lonFumo = lon_fumo
                    textResult.text = "Distanza fumo: ${"%.2f".format(D_fumo)} m\n" +
                            "Lat: ${"%.6f".format(latFumo)}, Lon: ${"%.6f".format(lonFumo)}"
                }
            } catch (e: Exception) {
                textResult.text = "Errore: Controlla i dati inseriti"
            }
        }

        btnShowMap.setOnClickListener {
            openMap()
        }
    }

    private fun metodo1(FOV_h: Double, W: Double, w_f: Double, w: Double): Double {
        val FOV_h_rad = Math.toRadians(FOV_h)
        return ((W + 9) / (2 * tan(FOV_h_rad / 2))) * (w_f / w)
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
}
