package defpackage.odometer

import com.google.android.gms.location.LocationSettingsStates

interface LocationListener {

    fun onLocationStates(states: LocationSettingsStates?)

    fun onLocationAvailability(available: Boolean)

    /**
     * @param speed in km/h
     * @param position in km
     */
    fun onTelemetryChanged(speed: Int, position: Float)
}