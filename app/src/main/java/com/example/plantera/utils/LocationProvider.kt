package com.example.plantera.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume

object LocationProvider {

    private const val TIMEOUT_MS = 15_000L

    suspend fun getCurrentLocation(context: Context): Pair<Double, Double>? {
        if (!hasLocationPermission(context)) return null

        val fused = withTimeoutOrNull(TIMEOUT_MS) {
            getLocationFromFused(context)
        }
        if (fused != null) return fused

        return withTimeoutOrNull(TIMEOUT_MS) {
            getLocationFromManager(context)
        }
    }


    @SuppressLint("MissingPermission")
    private suspend fun getLocationFromFused(context: Context): Pair<Double, Double>? {
        return try {
            val client = LocationServices.getFusedLocationProviderClient(context)

            suspendCancellableCoroutine { cont ->
                client.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null)
                    .addOnSuccessListener { location ->
                        if (location != null) {
                            cont.resume(Pair(location.latitude, location.longitude))
                        } else {
                            cont.resume(null)
                        }
                    }
                    .addOnFailureListener {
                        cont.resume(null)
                    }
            }
        } catch (e: Exception) {
            null
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun getLocationFromManager(context: Context): Pair<Double, Double>? {
        return try {
            val manager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
                ?: return null

            val lastKnown = getLastKnownLocation(manager)
            if (lastKnown != null) return lastKnown

            suspendCancellableCoroutine { cont ->
                val listener = object : android.location.LocationListener {
                    override fun onLocationChanged(location: Location) {
                        cont.resume(Pair(location.latitude, location.longitude))
                    }
                    override fun onStatusChanged(provider: String?, status: Int, extras: android.os.Bundle?) {}
                    override fun onProviderEnabled(provider: String) {}
                    override fun onProviderDisabled(provider: String) {}
                }

                val provider = when {
                    manager.isProviderEnabled(LocationManager.GPS_PROVIDER) ->
                        LocationManager.GPS_PROVIDER
                    manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ->
                        LocationManager.NETWORK_PROVIDER
                    else -> null
                }

                if (provider == null) {
                    cont.resume(null)
                    return@suspendCancellableCoroutine
                }

                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        manager.getCurrentLocation(
                            provider,
                            null,
                            context.mainExecutor,
                            { location ->
                                if (location != null) {
                                    cont.resume(Pair(location.latitude, location.longitude))
                                } else {
                                    cont.resume(null)
                                }
                            }
                        )
                    } else {
                        manager.requestLocationUpdates(
                            provider,
                            0L,
                            0f,
                            listener,
                            Looper.getMainLooper()
                        )
                    }
                } catch (e: SecurityException) {
                    cont.resume(null)
                }

                cont.invokeOnCancellation {
                    try {
                        manager.removeUpdates(listener)
                    } catch (_: Exception) {}
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation(manager: LocationManager): Pair<Double, Double>? {
        val providers = listOf(
            LocationManager.GPS_PROVIDER,
            LocationManager.NETWORK_PROVIDER,
            LocationManager.PASSIVE_PROVIDER
        )

        var best: Location? = null
        for (provider in providers) {
            try {
                val loc = manager.getLastKnownLocation(provider) ?: continue
                if (best == null || loc.time > best.time) {
                    best = loc
                }
            } catch (_: Exception) {}
        }

        return best?.let { Pair(it.latitude, it.longitude) }
    }

    private fun hasLocationPermission(context: Context): Boolean {
        val fine = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }
}