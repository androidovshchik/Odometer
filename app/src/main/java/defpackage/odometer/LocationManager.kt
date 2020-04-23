package defpackage.odometer

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnSuccessListener
import defpackage.odometer.extensions.add
import defpackage.odometer.extensions.areGranted
import defpackage.odometer.extensions.pseudoElapsedTime
import defpackage.odometer.extensions.shiftLeft
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.ref.WeakReference

const val REQUEST_LOCATION = 123

// in millis
const val MEASURE_TIME = 10_000L

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

    private val locationTime = LocationTime()

    init {
        require(MEASURE_TIME / LOCATION_TIME in 2..timeArray.size) {
            "At least 2 measurements are required in time and less than the size of the array"
        }
        System.loadLibrary("main")
        timeArray.fill(-1L)
        distanceArray.fill(-1f)
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
            val speed = withContext(Dispatchers.Default) {
                val distance = locationTime.getDistance(location)
                val pseudoNow = pseudoElapsedTime()
                timeArray.forEachIndexed { i, value ->
                    if (value < pseudoNow - MEASURE_TIME) {
                        timeArray[i] = -1L
                        distanceArray[i] = -1f
                    }
                }
                timeArray.shiftLeft()
                distanceArray.shiftLeft()
                timeArray.add(pseudoNow)
                distanceArray.add(distance)
                val size = timeArray.indexOfFirst { it < 0L }
                    .let { if (it < 0) timeArray.size else it }
                if (BuildConfig.DEBUG) {
                    Timber.d("size $size")
                    Timber.d(timeArray.toList().toString())
                    Timber.d(distanceArray.toList().toString())
                }
                getSpeed(BuildConfig.DEBUG, size, timeArray, distanceArray)
            }
            reference?.get()?.onSpeedChanged(speed)
        }
    }

    fun removeUpdates() {
        fusedClient.removeLocationUpdates(locationCallback)
        job.cancelChildren()
    }

    fun release() {
        reference?.clear()
    }

    /**
     * @param time in millis
     * @param distances in meters
     * @return speed in km/h
     */
    private external fun getSpeed(
        log: Boolean,
        size: Int,
        time: LongArray,
        distances: FloatArray
    ): Int

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