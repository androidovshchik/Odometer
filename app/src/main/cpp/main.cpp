#include <jni.h>
#include <android/log.h>
#include <cmath>

#define TAG "CPP"

#define d(log, ...) if (log) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)
#define i(log, ...) if (log) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)
#define w(log, ...) if (log) __android_log_print(ANDROID_LOG_WARN, TAG, __VA_ARGS__)
#define e(log, ...) if (log) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)

/**
 * @param time in millis
 * @param distances in meters
 * @return speed in km/h
 */
extern "C"
JNIEXPORT jint JNICALL
Java_defpackage_odometer_LocationManager_getSpeed(JNIEnv *env, jobject, jboolean log, jint size,
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
    for (int i = 1; i < size; i++) {
        float speed = y[i] / (x[i] - x[i - 1]);
        sumX += x[i];
        sumY += speed;
        sumX2 += x[i] * x[i];
        sumXY += x[i] * speed;
    }
    // y = a * x + b
    float a = (size * sumXY - (sumX * sumY)) / (size * sumX2 - sumX * sumX);
    float b = (sumY - a * sumX) / size;
    int speed = (int) round((a * x[size - 1] + b) * 3600);
    env->ReleaseLongArrayElements(time, x, 0);
    env->ReleaseFloatArrayElements(distances, y, 0);
    return speed;
}