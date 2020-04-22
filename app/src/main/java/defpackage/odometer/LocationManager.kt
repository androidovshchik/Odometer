package defpackage.odometer

import android.Manifest
import android.app.Activity
import android.content.Context
import android.location.Location
import android.os.SystemClock
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import defpackage.odometer.extensions.areGranted
import defpackage.odometer.extensions.copyToArray
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.ref.WeakReference
import kotlin.math.min

const val REQUEST_LOCATION = 123

// in millis
private const val MEASURE_TIME = 10_000L

class LocationManager(context: Context) : CoroutineScope {

    private var reference: WeakReference<LocationListener>? = null

    private val fusedClient = LocationServices.getFusedLocationProviderClient(context)

    private val timeArray = LongArray(10)

    private val distancesArray = FloatArray(10)

    private val timeList = mutableListOf<Long>()

    private val distancesList = mutableListOf<Float>()

    private var lastLocation: Location? = null

    private val job = SupervisorJob()

    init {
        System.loadLibrary("main")
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
    fun requestUpdates(context: Context, interval: Long) {
        // get at least 2 points and less than max count
        require(MEASURE_TIME / interval in 2..timeArray.size)
        if (!context.areGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            if (context is Activity) {
                ActivityCompat.requestPermissions(
                    context, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION
                )
            }
            return
        }
        val request = LocationRequest.create()
            .setInterval(interval)
            .setFastestInterval(interval)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        fusedClient.requestLocationUpdates(request, locationCallback, null)
        if (context is Activity) {
            LocationServices.getSettingsClient(context)
                .checkLocationSettings(
                    LocationSettingsRequest.Builder()
                        .addLocationRequest(request)
                        .setAlwaysShow(true)
                        .build()
                )
                .addOnSuccessListener {
                    onLocationState(it.locationSettingsStates)
                }
                .addOnFailureListener {
                    onLocationState(null)
                    if (it is ResolvableApiException) {
                        try {
                            it.startResolutionForResult(
                                this,
                                REQUEST_LOCATION
                            )
                        } catch (e: Throwable) {
                            Timber.e(e)
                        }
                    } else {
                        Timber.e(it)
                    }
                }
        }
    }

    private fun onLocationChanged(location: Location) {
        job.cancelChildren()
        launch {
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
                val size = min(timeArray.size, timeList.size)
                val speed = withContext(Dispatchers.Default) {
                    getSpeed(size, timeArray, distancesArray)
                }
                reference?.get()?.onSpeedChanged(speed)
            }
            lastLocation = location
        }
    }

    fun removeUpdates() {
        fusedClient.removeLocationUpdates(locationCallback)
        job.cancelChildren()
        lastLocation = null
        timeList.clear()
        distancesList.clear()
    }

    /**
     * @param time in millis
     * @param distances in meters
     * @return speed in km/h
     */
    private external fun getSpeed(size: Int, time: LongArray, distances: FloatArray): Int

    override val coroutineContext = Dispatchers.Main + job + CoroutineExceptionHandler { _, e ->
        Timber.e(e)
    }

    private val locationCallback = object : LocationCallback() {

        override fun onLocationAvailability(availability: LocationAvailability) {
            reference?.get()?.onLocationAvailability(availability.isLocationAvailable)
        }

        override fun onLocationResult(result: LocationResult) {
            onLocationChanged(result.lastLocation ?: return)
        }
    }
}

interface LocationListener {

    fun onLocationAvailability(available: Boolean)

    /**
     * @param speed in km/h
     */
    fun onSpeedChanged(speed: Int)
}