package defpackage.odometer

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.SystemClock
import com.google.android.gms.location.*
import java.lang.ref.WeakReference
import kotlin.math.min

// in millis
private const val MEASURE_TIME = 10_000L

@SuppressLint("MissingPermission")
@Suppress("MemberVisibilityCanBePrivate", "DEPRECATION")
class LocationManager(context: Context) {

    private var reference: WeakReference<LocationListener>? = null

    private val fusedClient = LocationServices.getFusedLocationProviderClient(context)

    private val time = mutableListOf<Long>()

    private val distances = mutableListOf<Float>()

    private var lastLocation: Location? = null

    init {
        System.loadLibrary("main")
    }

    init {
        /*listener.apply {
            onLocationAvailability(gpsClient.isProviderEnabled(LocationManager.GPS_PROVIDER))
            gpsClient.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let {
                onLocationChanged(it, satellitesCount)
            }
        }*/
    }

    /**
     * It is assumed that this will be called one time or never
     */
    fun setLocationListener(listener: LocationListener) {
        reference = WeakReference(listener)
    }

    /**
     * @param interval in millis
     */
    fun requestUpdates(interval: Long) {
        require(interval < MEASURE_TIME / 2)
        val request = LocationRequest.create()
            .setInterval(interval)
            .setFastestInterval(interval)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        fusedClient.requestLocationUpdates(request, locationCallback, null)
    }

    private fun onLocationChanged(location: Location) {
        lastLocation?.let { lastLocation ->
            val output = FloatArray(2)
            Location.distanceBetween(
                lastLocation.latitude,
                lastLocation.longitude,
                location.latitude,
                location.longitude,
                output
            )
            val now = SystemClock.elapsedRealtime()
            val iterator = time.iterator()
            for ((i, x) in iterator.withIndex()) {
                if (x < now - MEASURE_TIME) {
                    iterator.remove()
                    distances.removeAt(i)
                }
            }
            time.add(now)
            distances.add(output[0])
            val size = min(time.size, distances.size)
            speedMap.put(SystemClock.elapsedRealtime(), output[0])
            getSpeed(0, time.toFloatArray(), distances.toFloatArray())
            reference?.get()?.onSpeedChanged(getSpeed(0, time!!, distances!!))
        }
        lastLocation = location
    }

    fun removeUpdates() {
        fusedClient.removeLocationUpdates(locationCallback)
        lastLocation = null
        time.clear()
        distances.clear()
    }

    private external fun getSpeed(size: Int, time: FloatArray, distances: FloatArray): Float

    private val locationCallback = object : LocationCallback() {

        override fun onLocationAvailability(availability: LocationAvailability) {

        }

        override fun onLocationResult(result: LocationResult) {
            onLocationChanged(result.lastLocation ?: return)
        }
    }
}

interface LocationListener {

    fun onSpeedChanged(speed: Float)
}