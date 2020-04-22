#include <jni.h>
#include <stdio.h>
#include <stdlib.h>

void getApprox(double **x, double *a, double *b, int n) {
    double sumx = 0;
    double sumy = 0;
    double sumx2 = 0;
    double sumxy = 0;
    for (int i = 0; i < n; i++) {
        sumx += x[0][i];
        sumy += x[1][i];
        sumx2 += x[0][i] * x[0][i];
        sumxy += x[0][i] * x[1][i];
    }
    *a = (n * sumxy - (sumx * sumy)) / (n * sumx2 - sumx * sumx);
    *b = (sumy - *a * sumx) / n;
    return;
}

extern "C"

JNIEXPORT jbyteArray JNICALL
Java_defpackage_odometer_CAESCBC_encrypt(JNIEnv *env, jobject, jfloatArray xs, jfloatArray ys) {
    double **x, a, b;
    int n;
    system("chcp 1251");
    system("cls");
    printf("Введите количество точек: ");
    scanf("%d", &n);
    x = getData(n);
    for (int i = 0; i < n; i++)
        printf("%5.1lf - %7.3lf\n", x[0][i], x[1][i]);
    getApprox(x, &a, &b, n);
    printf("a = %lf\nb = %lf", a, b);
    uint8_t *dataBytes = (uint8_t *) env->GetByteArrayElements(data, 0);
    uint8_t *keyBytes = (uint8_t *) env->GetByteArrayElements(key, 0);
    uint8_t *ivBytes = (uint8_t *) env->GetByteArrayElements(iv, 0);
    struct AES_ctx ctx;
    AES_init_ctx_iv(&ctx, keyBytes, ivBytes);
    AES_CBC_encrypt_buffer(&ctx, dataBytes, (uint32_t) env->GetArrayLength(data));
    env->ReleaseByteArrayElements(data, (jbyte *) dataBytes, 0);
    env->ReleaseByteArrayElements(key, (jbyte *) keyBytes, 0);
    env->ReleaseByteArrayElements(iv, (jbyte *) ivBytes, 0);
    return data;
}