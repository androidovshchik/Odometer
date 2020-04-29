/*
 * Calculates the distance of two points (from, to) in meter, using latitude and longitude, using Haversine formula.
 * Reference: https://en.wikipedia.org/wiki/Haversine_formula
 */
#include <jni.h>
#include <cmath>

double toRadians(double degree) {
    return degree * M_PI / 180;
}

double getDistance(double lat1, double lon1, double lat2, double lon2) {
    int d = 2 * 6378137;
    lat1 = toRadians(lat1);
    lon1 = toRadians(lon1);
    lat2 = toRadians(lat2);
    lon2 = toRadians(lon2);
    double dLat = lat2 - lat1;
    double dLon = lon2 - lon1;
    return d * asin(sqrt(pow(sin(dLat / 2), 2) + cos(lat1) * cos(lat2) * pow(sin(dLon / 2), 2)));
}

extern "C"
JNIEXPORT jdouble JNICALL
Java_defpackage_odometer_LocationManager_getDistance(JNIEnv *env, jobject, jdouble lat1,
                                                     jdouble lon1, jdouble lat2, jdouble lon2) {
    return getDistance(lat1, lon1, lat2, lon2);
}