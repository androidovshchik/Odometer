package defpackage.odometer

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.SystemClock
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnSuccessListener
import defpackage.odometer.extensions.areGranted
import defpackage.odometer.extensions.copyToArray
import kotlinx.coroutines.*
import org.jetbrains.anko.locationManager
import timber.log.Timber
import java.lang.ref.WeakReference
import kotlin.math.min

const val REQUEST_LOCATION = 123

// in millis
private const val MEASURE_TIME = 10_000L

// in millis
private const val LOCATION_TIME = 3000L

@SuppressLint("MissingPermission")
class LocationManager(context: Context) : CoroutineScope,
    OnSuccessListener<LocationSettingsResponse> {

    private var reference: WeakReference<LocationListener>? = null

    private val fusedClient = LocationServices.getFusedLocationProviderClient(context)

    private val job = SupervisorJob()

    private val timeArray = LongArray(10)

    private val distanceArray = FloatArray(10)

    private val timeList = mutableListOf<Long>()

    private val distanceList = mutableListOf<Float>()

    private var lastLocation: Location? = null

    init {
        require(MEASURE_TIME / LOCATION_TIME in 2..timeArray.size) {
            "At least 2 measurements are required in time and less than the size of the array"
        }
        System.loadLibrary("main")
        if (context.areGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            val locationManager = context.locationManager
            val isGpsAvailable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkAvailable =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            reference?.get()?.onLocationAvailability(isGpsAvailable || isNetworkAvailable)
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (location != null) {
                onLocationChanged(location)
            }
        }
    }

    /**
     * It is assumed that this will be called one time or never
     */
    fun setLocationListener(listener: LocationListener) {
        reference = WeakReference(listener)
    }

    fun requestUpdates(context: Context) {
        if (!context.areGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            if (context is Activity) {
                ActivityCompat.requestPermissions(
                    context, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION
                )
            }
            return
        }
        timeList.add(SystemClock.elapsedRealtime())
        distanceList.add(0f)
        val request = LocationRequest.create()
            .setInterval(LOCATION_TIME)
            .setFastestInterval(LOCATION_TIME)
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
                .addOnSuccessListener(this)
                .addOnFailureListener {
                    reference?.get()?.onLocationAvailability(false)
                    if (it is ResolvableApiException) {
                        try {
                            it.startResolutionForResult(context, REQUEST_LOCATION)
                        } catch (e: Throwable) {
                            Timber.e(e)
                        }
                    } else {
                        Timber.e(it)
                    }
                }
        }
    }

    override fun onSuccess(locationSettings: LocationSettingsResponse) {
        reference?.get()?.onLocationStates(locationSettings.locationSettingsStates)
    }

    private fun onLocationChanged(location: Location) {
        job.cancelChildren()
        launch {
            lastLocation?.let { lastLocation ->
                val speed = withContext(Dispatchers.Default) {
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
                    for ((i, time) in iterator.withIndex()) {
                        if (time < now - MEASURE_TIME) {
                            iterator.remove()
                            distanceList.removeAt(i)
                        }
                    }
                    timeList.add(now)
                    distanceList.add(output[0])
                    timeList.copyToArray(timeArray, -1L)
                    distanceList.copyToArray(distanceArray, 0f)
                    val size = min(timeArray.size, timeList.size)
                    getSpeed(size, timeArray, distanceArray)
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
        distanceList.clear()
    }

    fun clear() {
        removeUpdates()
        reference?.clear()
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

    fun onLocationStates(states: LocationSettingsStates?)

    fun onLocationAvailability(available: Boolean)

    /**
     * @param speed in km/h
     */
    fun onSpeedChanged(speed: Int)
}