package defpackage.odometer

import android.location.Location
import android.os.SystemClock

class LocationTime {

    private var lat = 0.0

    private var lon = 0.0

    private var lastTime = Long.MIN_VALUE

    private val output = FloatArray(1)

    init {
        output.fill(0f)
    }

    /**
     * @return distance in meters
     */
    fun getDistance(location: Location): Float {
        val now = SystemClock.elapsedRealtime()
        if (lastTime >= now - MEASURE_TIME) {
            Location.distanceBetween(lat, lon, location.latitude, location.longitude, output)
        } else {
            output.fill(0f)
        }
        lat = location.latitude
        lon = location.longitude
        lastTime = now
        return output[0]
    }
}