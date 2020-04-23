#include <jni.h>
#include <android/log.h>
#include <cmath>

#define TAG "CPP"

#define d(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)
#define i(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)
#define w(...) __android_log_print(ANDROID_LOG_WARN, TAG, __VA_ARGS__)
#define e(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)

#define mms2kmh(speed) (int) round(speed * 3600)

/**
 * Linear approximation with method of the least squares
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
    if (size == 2) {
        int64_t duration = x[1] - x[0];
        float speed;
        if (duration > 0) {
            speed = y[1] / duration;
        } else {
            speed = 0;
        }
        env->ReleaseLongArrayElements(time, x, 0);
        env->ReleaseFloatArrayElements(distances, y, 0);
        return mms2kmh(speed);
    }
    float sumX = 0;
    float sumY = 0;
    float sumX2 = 0;
    float sumXY = 0;
    for (int i = 1; i < size; i++) {
        int64_t duration = x[i] - x[i - 1];
        float speed;
        if (duration > 0) {
            speed = y[i] / duration;
        } else {
            speed = 0;
        }
        if (log) {
            d("speed[%i] = %f km/h", i, speed * 3600);
        }
        sumX += x[i];
        sumY += speed;
        sumX2 += x[i] * x[i];
        sumXY += x[i] * speed;
    }
    int count = size - 1;
    // y = a * x + b
    float denominator = count * sumX2 - sumX * sumX;
    float a;
    if (denominator != 0) {
        a = (count * sumXY - sumX * sumY) / denominator;
    } else {
        a = 0;
    }
    float b = (sumY - a * sumX) / count;
    int64_t moment = x[size - 1];
    if (log) {
        d("y = %f * %jd + %f", a, moment, b);
    }
    float speed = a * moment + b;
    env->ReleaseLongArrayElements(time, x, 0);
    env->ReleaseFloatArrayElements(distances, y, 0);
    return mms2kmh(speed);
}