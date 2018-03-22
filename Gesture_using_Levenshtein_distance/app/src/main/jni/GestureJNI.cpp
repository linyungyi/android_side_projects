//
// Created by linyu on 2016/6/13.
//

#include "com_my_gesture_GestureJNI.h"

#include <android/log.h>
#define  LOG_TAG    "[ZET-JNI]"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

#define MIN3(a, b, c) ((a) < (b) ? ((a) < (c) ? (a) : (c)) : ((b) < (c) ? (b) : (c)))


JNIEXPORT jstring JNICALL Java_com_my_gesture_GestureJNI_hello
        (JNIEnv *env, jclass)
{
    return (*env).NewStringUTF("Hello JNI for gesture - powered by Albert Lin"); ///< Print Debug message
}

JNIEXPORT jint JNICALL Java_com_my_gesture_GestureJNI_editDistance
        (JNIEnv* env, jclass thiz, jint lengthTarget, jint lengthSource, jbyteArray bufTarget, jbyteArray bufSource)
{
    int ret;
    jboolean copy;

    unsigned int s1len, s2len, x, y, lastdiag, olddiag;
    s1len = lengthTarget;
    s2len = lengthSource;

    jbyte* buffer1 = (*env).GetByteArrayElements(bufTarget, &copy);
    jbyte* buffer2 = (*env).GetByteArrayElements(bufSource, &copy);

    unsigned int column[s1len+1];
    for (y = 1; y <= s1len; y++)
        column[y] = y;
    for (x = 1; x <= s2len; x++) {
        column[0] = x;
        for (y = 1, lastdiag = x-1; y <= s1len; y++) {
            olddiag = column[y];
            column[y] = MIN3(column[y] + 1, column[y-1] + 1, lastdiag + (buffer1[y-1] == buffer2[x-1] ? 0 : 1));
            lastdiag = olddiag;
        }
    }

    ret = column[s1len];

    (*env).ReleaseByteArrayElements(bufTarget, buffer1, JNI_ABORT);
    (*env).ReleaseByteArrayElements(bufSource, buffer2, JNI_ABORT);
    return ret;
}