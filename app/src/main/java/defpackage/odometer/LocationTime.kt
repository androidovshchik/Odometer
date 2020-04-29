package defpackage.odometer

import android.location.Location
import defpackage.odometer.extension.pseudoElapsedTime

class LocationTime {

    private var lat = 0.0

    private var lon = 0.0

    private var lastTime = -1L

    init {
        System.loadLibrary("main")
    }

    /**
     * @return distance in meters
     */
    fun getDistance(location: Location): Float {
        var distance = 0f
        val pseudoNow = pseudoElapsedTime()
        if (lastTime >= pseudoNow - MEASURE_TIME) {
            distance = getDistance(lat, lon, location.latitude, location.longitude)
        }
        lat = location.latitude
        lon = location.longitude
        lastTime = pseudoNow
        return distance
    }

    private external fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float
}