/*
 * Calculates the distance of two points (from, to) in meter, using latitude and longitude, using Haversine formula.
 * Reference: https://en.wikipedia.org/wiki/Haversine_formula
 */
#include <math.h>

double toRadians(double degree) {
    return degree * M_PI / 180;
}

double getDistance(double lat1, double lon1, double lat2, double lon2) {
    // earth radius in meter
    int r = 6378137;
    lat1 = toRadians(lat1);
    lon1 = toRadians(lon1);
    lat2 = toRadians(lat2);
    lon2 = toRadians(lon2);
    double dLat = lat2 - lat1;
    double dLon = lon2 - lon1;
    return 2 * r *
           asin(sqrt(pow(sin(dLat / 2), 2) + cos(lat1) * cos(lat2) * pow(sin(dLon / 2), 2)));
}