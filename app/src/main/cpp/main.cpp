#include <jni.h>

extern "C"

JNIEXPORT jfloatArray JNICALL
Java_defpackage_odometer_LocationManager_approximate(JNIEnv *env, jobject, jint size,
                                                     jfloatArray xa, jfloatArray ya) {
    jfloat *x = env->GetFloatArrayElements(xa, 0);
    jfloat *y = env->GetFloatArrayElements(ya, 0);
    float sumX = 0;
    float sumY = 0;
    float sumX2 = 0;
    float sumXY = 0;
    for (int i = 0; i < size; i++) {
        sumX += x[i];
        sumY += y[i];
        sumX2 += x[i] * x[i];
        sumXY += x[i] * y[i];
    }
    jfloat result[2];
    // y = A * x + b
    result[0] = (size * sumXY - (sumX * sumY)) / (size * sumX2 - sumX * sumX);
    // y = a * x + B
    result[1] = (sumY - result[0] * sumX) / size;
    jfloatArray output = env->NewFloatArray(2);
    env->SetFloatArrayRegion(output, 0, 2, result);
    env->ReleaseFloatArrayElements(xa, x, 0);
    env->ReleaseFloatArrayElements(ya, y, 0);
    return output;
}