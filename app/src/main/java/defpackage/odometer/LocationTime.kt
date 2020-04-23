package defpackage.odometer

import android.location.Location
import android.os.SystemClock

class LocationTime {

    var lat = 0.0

    var lon = 0.0

    var elapsedTime = -1L

    private val output = FloatArray(2)

    /**
     * @return distance in meters
     */
    fun update(location: Location): Float {
        val now = SystemClock.elapsedRealtime()
        Location.distanceBetween(lat, lon, location.latitude, location.longitude, output)
        return output[0]
    }

    fun clear() {

    }
}