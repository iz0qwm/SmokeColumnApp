package com.example.smokecolumnapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.*

class MainActivity : AppCompatActivity() {

    private var latDrone: Double = 0.0
    private var lonDrone: Double = 0.0
    private var latFumo: Double = 0.0
    private var lonFumo: Double = 0.0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editFOV = findViewById<EditText>(R.id.editFOV)
        val editW = findViewById<EditText>(R.id.editW)
        val editWf = findViewById<EditText>(R.id.editWf)
        val editw = findViewById<EditText>(R.id.editw)
        val editLat = findViewById<EditText>(R.id.editLat)
        val editLon = findViewById<EditText>(R.id.editLon)
        val editHeading = findViewById<EditText>(R.id.editHeading)
        val btnCalcola = findViewById<Button>(R.id.btnCalcola)
        val btnShowMap = findViewById<Button>(R.id.btnShowMap)
        val textResult = findViewById<TextView>(R.id.textResult)

        btnCalcola.setOnClickListener {
            try {
                val FOV_h = editFOV.text.toString().toDouble()
                val W = editW.text.toString().toDouble()
                val w_f = editWf.text.toString().toDouble()
                val w = editw.text.toString().toDouble()
                latDrone = editLat.text.toString().toDouble()
                lonDrone = editLon.text.toString().toDouble()
                val heading = editHeading.text.toString().toDouble()

                // Calcolo della distanza
                val D_fumo = metodo1(FOV_h, W, w_f, w)

                // Calcolo coordinate della colonna di fumo
                val (lat_fumo, lon_fumo) = calcolaCoordinateFumo(latDrone, lonDrone, D_fumo, heading)

                // Aggiorna le coordinate della colonna di fumo
                latFumo = lat_fumo
                lonFumo = lon_fumo

                // Mostra il risultato
                textResult.text = "Distanza fumo: ${"%.2f".format(D_fumo)} m\n" +
                        "Lat: ${"%.6f".format(latFumo)}, Lon: ${"%.6f".format(lonFumo)}"

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

    private fun calcolaCoordinateFumo(lat_drone: Double, lon_drone: Double, D_fumo: Double, heading: Double): Pair<Double, Double> {
        val heading_rad = Math.toRadians(heading)
        val lat_fumo = lat_drone + (D_fumo * cos(heading_rad)) / 111320
        val lon_fumo = lon_drone + (D_fumo * sin(heading_rad)) / (111320 * cos(Math.toRadians(lat_drone)))
        return Pair(lat_fumo, lon_fumo)
    }

    private fun openMap() {
        val intent = Intent(this, MapActivity::class.java).apply {
            putExtra("LAT_DRONE", latDrone)
            putExtra("LON_DRONE", lonDrone)
            putExtra("LAT_FUMO", latFumo)
            putExtra("LON_FUMO", lonFumo)
        }
        startActivity(intent)
    }
}