package defpackage.odometer

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.SystemClock
import com.google.android.gms.location.*
import defpackage.odometer.extensions.copyToArray
import java.lang.ref.WeakReference

// in millis
private const val MEASURE_TIME = 10_000L

private const val POINTS_COUNT = 10

@SuppressLint("MissingPermission")
@Suppress("MemberVisibilityCanBePrivate", "DEPRECATION")
class LocationManager(context: Context) {

    private var reference: WeakReference<LocationListener>? = null

    private val fusedClient = LocationServices.getFusedLocationProviderClient(context)

    private val timeArray = LongArray(POINTS_COUNT)

    private val distancesArray = FloatArray(POINTS_COUNT)

    private val timeList = mutableListOf<Long>()

    private val distancesList = mutableListOf<Float>()

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
        // get at least 2 points and less than max count
        require(MEASURE_TIME / interval in 2..POINTS_COUNT)
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
            val iterator = timeList.iterator()
            for ((i, x) in iterator.withIndex()) {
                if (x < now - MEASURE_TIME) {
                    iterator.remove()
                    distancesList.removeAt(i)
                }
            }
            timeList.add(now)
            distancesList.add(output[0])
            timeList.copyToArray(timeArray, -1L)
            distancesList.copyToArray(distancesArray, 0f)
            getSpeed(timeList.size, timeArray, distancesArray)
            reference?.get()?.onSpeedChanged(getSpeed(0, time, distances))
        }
        lastLocation = location
    }

    fun removeUpdates() {
        fusedClient.removeLocationUpdates(locationCallback)
        lastLocation = null
        timeList.clear()
        distancesList.clear()
    }

    private external fun getSpeed(size: Int, time: LongArray, distances: FloatArray): Float

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