package com.example.smokecolumnapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

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
        val latDrone = intent.getDoubleExtra("LAT_DRONE", 0.0)
        val lonDrone = intent.getDoubleExtra("LON_DRONE", 0.0)
        val latFumo = intent.getDoubleExtra("LAT_FUMO", 0.0)
        val lonFumo = intent.getDoubleExtra("LON_FUMO", 0.0)

        // Crea le posizioni
        val dronePos = LatLng(latDrone, lonDrone)
        val fumoPos = LatLng(latFumo, lonFumo)

        // Aggiungi i marker sulla mappa
        googleMap.addMarker(MarkerOptions().position(dronePos).title("Drone"))
        googleMap.addMarker(MarkerOptions().position(fumoPos).title("Colonna di Fumo"))

        // Centra la mappa sul drone
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dronePos, 15f))
    }
}