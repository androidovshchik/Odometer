/*
 * Calculates the distance of two points (from, to) in meter, using latitude and longitude, using Haversine formula.
 * Reference: https://en.wikipedia.org/wiki/Haversine_formula
 */
#include <jni.h>
#include <cmath>

#define rad(degree) degree * M_PI / 180;

double getDistance(double lat1, double lon1, double lat2, double lon2) {
    int d = 2 * 6378137;
    lat1 = rad(lat1);
    lon1 = rad(lon1);
    lat2 = rad(lat2);
    lon2 = rad(lon2);
    double dLat = lat2 - lat1;
    double dLon = lon2 - lon1;
    return d * asin(sqrt(pow(sin(dLat / 2), 2) + cos(lat1) * cos(lat2) * pow(sin(dLon / 2), 2)));
}

extern "C"
JNIEXPORT jfloat JNICALL
Java_defpackage_odometer_LocationTime_getDistance(JNIEnv *env, jobject, jdouble lat1,
                                                  jdouble lon1, jdouble lat2, jdouble lon2) {
    return (float) getDistance(lat1, lon1, lat2, lon2);
}