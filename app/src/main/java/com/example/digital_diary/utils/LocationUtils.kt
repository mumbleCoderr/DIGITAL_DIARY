package com.example.digital_diary.utils

import android.app.Activity
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.util.Log
import com.google.android.gms.location.LocationServices
import java.util.*

object LocationUtils {
    fun getLastKnownLocation(activity: Activity, onLocationResult: (Location?) -> Unit) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        if (activity.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) ==
            android.content.pm.PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    onLocationResult(location)
                }
                .addOnFailureListener {
                    Log.e("LocationUtils", "Failed to get location", it)
                    onLocationResult(null)
                }
        } else {
            onLocationResult(null)
        }
    }

    fun getCityName(context: Context, location: Location?): String? {
        if (location == null) return null
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            addresses?.get(0)?.locality ?: addresses?.get(0)?.subAdminArea ?: "Unknown city"
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
