#include <jni.h>
#include <cmath>

/**
 * @param time in millis
 * @param distances in meters
 * @return speed in km/h
 */
extern "C"
JNIEXPORT jint JNICALL
Java_defpackage_odometer_LocationManager_getSpeed(JNIEnv *env, jobject, jint size,
                                                  jlongArray time, jfloatArray distances) {
    if (size < 2) {
        return 0;
    }
    jlong *x = env->GetLongArrayElements(time, 0);
    jfloat *y = env->GetFloatArrayElements(distances, 0);
    float sumX = 0;
    float sumY = 0;
    float sumX2 = 0;
    float sumXY = 0;
    for (int i = 0; i < size - 1; i++) {
        float speed = y[i] / (x[i + 1] - x[i]);
        sumX += x[i];
        sumY += speed;
        sumX2 += x[i] * x[i];
        sumXY += x[i] * speed;
    }
    // y = a * x + b
    float a = (size * sumXY - (sumX * sumY)) / (size * sumX2 - sumX * sumX);
    float b = (sumY - a * sumX) / size;
    int speed = (int) round((a * x[size - 1] + b) * 360);
    env->ReleaseLongArrayElements(time, x, 0);
    env->ReleaseFloatArrayElements(distances, y, 0);
    return speed;
}