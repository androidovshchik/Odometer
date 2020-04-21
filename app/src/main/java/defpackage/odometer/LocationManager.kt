package defpackage.odometer

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.util.SparseIntArray
import com.google.android.gms.location.*
import java.lang.ref.WeakReference

@SuppressLint("MissingPermission")
@Suppress("MemberVisibilityCanBePrivate", "DEPRECATION")
class LocationManager(context: Context) {

    private var reference: WeakReference<LocationListener>? = null

    private val fusedClient = LocationServices.getFusedLocationProviderClient(context)

    /**
     * Keys are duration in seconds, values are speed in km/h
     */
    private val speedMap = SparseIntArray()

    private var lastLocation: Location? = null

    init {
        listener.apply {
            onLocationAvailability(gpsClient.isProviderEnabled(LocationManager.GPS_PROVIDER))
            gpsClient.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let {
                onLocationChanged(it, satellitesCount)
            }
        }
    }

    /**
     * It is assumed that this will be called one time or never
     */
    fun setLocationListener(listener: LocationListener) {
        reference = WeakReference(listener)
    }

    fun requestUpdates(interval: Long) {
        fusedClient.requestLocationUpdates(LocationRequest.create().also {
            it.interval = interval
            it.fastestInterval = interval
            it.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }, locationCallback, null)
    }

    private fun onLocationChanged(location: Location) {
        lastLocation?.let {
            val output = FloatArray(2)
            Location.distanceBetween(
                it.latitude,
                it.longitude,
                location.latitude,
                location.longitude,
                output
            )
            val distance = output[0]
        }
        lastLocation = location
    }

    fun removeUpdates() {
        fusedClient.removeLocationUpdates(locationCallback)
        lastLocation = null
        speedMap.clear()
    }

    private val locationCallback = object : LocationCallback() {

        override fun onLocationAvailability(availability: LocationAvailability) {

        }

        override fun onLocationResult(result: LocationResult) {
            onLocationChanged(result.lastLocation ?: return)
        }
    }
}

interface LocationListener {

    fun onSpeedChanged(speed: Int)
}