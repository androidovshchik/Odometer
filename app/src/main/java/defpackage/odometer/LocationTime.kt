package defpackage.odometer

import android.location.Location
import defpackage.odometer.extensions.pseudoElapsedTime

class LocationTime {

    private var lat = 0.0

    private var lon = 0.0

    private var lastTime = -1L

    private val output = FloatArray(1)

    init {
        output.fill(0f)
    }

    /**
     * @return distance in meters
     */
    fun getDistance(location: Location): Float {
        val pseudoNow = pseudoElapsedTime()
        if (lastTime >= pseudoNow - MEASURE_TIME) {
            Location.distanceBetween(lat, lon, location.latitude, location.longitude, output)
        } else {
            output.fill(0f)
        }
        lat = location.latitude
        lon = location.longitude
        lastTime = pseudoNow
        return output[0]
    }
}