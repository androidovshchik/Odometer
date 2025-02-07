@file:Suppress("DEPRECATION")

package defpackage.odometer.screen.main

import com.google.android.gms.location.LocationSettingsStates
import defpackage.odometer.LocationListener
import defpackage.odometer.screen.base.BaseFragment

abstract class LocationFragment : BaseFragment(), LocationListener {

    override fun onLocationStates(states: LocationSettingsStates?) {
        throw IllegalAccessException("Should not be called")
    }

    override fun onLocationAvailability(available: Boolean) {
    }

    override fun onTelemetryChanged(speed: Int, position: Float) {
    }
}