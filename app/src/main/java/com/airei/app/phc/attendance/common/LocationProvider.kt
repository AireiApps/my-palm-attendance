package com.airei.app.phc.attendance.common

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationProvider(private val context: Context) {

    private var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    interface LocationListener {
        fun onLocationResult(location: Location)
    }

    fun getLastLocation(listener: LocationListener) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.i("LocationProvider", "getLastLocation: no permission")
            return
        }
        Log.i("LocationProvider", "getLastLocation: permission granted")

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    listener.onLocationResult(it)
                }
            }
            .addOnFailureListener { e ->
                // Handle failure to get last known location
            }

    }
}


