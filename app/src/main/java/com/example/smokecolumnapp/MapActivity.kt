package com.example.smokecolumnapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLngBounds


class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Trova il fragment della mappa e inizializzalo
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        // Prendi le coordinate passate dall'Intent
        val latDrone1 = intent.getDoubleExtra("LAT_DRONE1", 0.0)
        val lonDrone1 = intent.getDoubleExtra("LON_DRONE1", 0.0)
        val latDrone2 = intent.getDoubleExtra("LAT_DRONE2", 0.0)
        val lonDrone2 = intent.getDoubleExtra("LON_DRONE2", 0.0)
        val latFumo = intent.getDoubleExtra("LAT_FUMO", 0.0)
        val lonFumo = intent.getDoubleExtra("LON_FUMO", 0.0)
        val useTwoDrones = intent.getBooleanExtra("USE_TWO_DRONES", false)

        // Crea le posizioni per i droni e la colonna di fumo
        val drone1Pos = LatLng(latDrone1, lonDrone1)
        val fumoPos = LatLng(latFumo, lonFumo)

        // Aggiungi i marker sulla mappa
        googleMap.addMarker(MarkerOptions().position(drone1Pos).title("Drone 1"))

        if (useTwoDrones) {
            val drone2Pos = LatLng(latDrone2, lonDrone2)
            googleMap.addMarker(MarkerOptions().position(drone2Pos).title("Drone 2"))
        }

        googleMap.addMarker(MarkerOptions().position(fumoPos).title("Colonna di Fumo"))

        // Centra la mappa sulla colonna di fumo o sul drone
        if (useTwoDrones) {
            val latLngBounds = LatLngBounds.builder()
                .include(drone1Pos)
                .include(LatLng(latDrone2, lonDrone2))
                .include(fumoPos)
                .build()
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100))
        } else {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(drone1Pos, 15f))
        }
    }
}
