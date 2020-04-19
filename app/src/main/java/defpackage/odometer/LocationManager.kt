package defpackage.odometer

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import com.google.android.gms.location.*
import timber.log.Timber
import java.lang.ref.WeakReference

@SuppressLint("MissingPermission")
@Suppress("MemberVisibilityCanBePrivate", "DEPRECATION")
class LocationManager(context: Context, listener: TelemetryListener) {

    private val reference = WeakReference(listener)

    private val fusedClient = LocationServices.getFusedLocationProviderClient(context)

    init {
        listener.apply {
            onLocationAvailability(gpsClient.isProviderEnabled(LocationManager.GPS_PROVIDER))
            gpsClient.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let {
                onLocationChanged(it, satellitesCount)
            }
        }
    }

    fun requestUpdates(interval: Long) {
        fusedClient.requestLocationUpdates(LocationRequest.create().also {
            it.interval = interval
            it.fastestInterval = interval
            it.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }, locationCallback, null)
    }

    fun removeUpdates() {
        fusedClient.removeLocationUpdates(locationCallback)
    }

    private val locationCallback = object : LocationCallback() {

        override fun onLocationAvailability(availability: LocationAvailability) {
            Timber.d("onLocationAvailability $availability")
            reference.get()?.onLocationAvailability(availability.isLocationAvailable)
        }

        override fun onLocationResult(result: LocationResult?) {
            result?.lastLocation?.let {
                Timber.i("Last location is $it")
                reference.get()?.onLocationResult(SimpleLocation(it))
            }
        }
    }
}