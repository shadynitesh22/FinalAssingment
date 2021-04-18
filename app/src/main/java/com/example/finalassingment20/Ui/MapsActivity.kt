package com.example.finalassingment20.Ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalassingment20.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private  var lstLatitudeLongitude = ArrayList<LatitudeLongitude>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        lstLatitudeLongitude.add(LatitudeLongitude(27.7159135, 85.3278387, "Naxal"))
        lstLatitudeLongitude.add(LatitudeLongitude(27.7145422, 85.3279627, "Khai kata po ho"))

        for (location in lstLatitudeLongitude) {
            mMap.addMarker(
                MarkerOptions().position(LatLng(location.latitude, location.longitude))
                    .title("Hamro College")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            )
        }
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(LatLng(27.7061943, 85.3300381), 10F), 4000, null
        )
        mMap.uiSettings.isZoomControlsEnabled = true
    }
}

