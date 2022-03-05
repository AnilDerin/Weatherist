package com.anilderin.weatherist.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat

const val MY_PERMISSIONS_REQUEST_LOCATION = 99

fun Activity.checkLocationPermission() {
    if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            AlertDialog.Builder(this)
                .setTitle("Location Permission Needed")
                .setMessage("This app needs the Location permission, please accept to use location functionality")
                .setPositiveButton(
                    "OK"
                ) { _, _ ->
                    requestLocationPermission()
                }
                .create()
                .show()
        } else {
            // No explanation needed, we can request the permission.
            requestLocationPermission()
        }
    }
}

fun Activity.requestLocationPermission(){
    ActivityCompat.requestPermissions(
        this,
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
        ),
        MY_PERMISSIONS_REQUEST_LOCATION
    )
}

