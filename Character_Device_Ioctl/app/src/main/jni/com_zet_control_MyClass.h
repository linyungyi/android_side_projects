/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_my_control_MyClass */

#ifndef _Included_com_my_control_MyClass
#define _Included_com_my_control_MyClass
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_my_control_MyClass
 * Method:    hello
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_my_control_MyClass_hello
  (JNIEnv *, jclass);

/*
 * Class:     com_my_control_MyClass
 * Method:    init
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_my_control_MyClass_init
  (JNIEnv *, jclass, jstring);

/*
 * Class:     com_my_control_MyClass
 * Method:    ioctl
 * Signature: (I[B)I
 */
JNIEXPORT jint JNICALL Java_com_my_control_MyClass_ioctl
  (JNIEnv *, jclass, jint, jbyteArray);

/*
 * Class:     com_my_control_MyClass
 * Method:    read
 * Signature: (I[B)I
 */
JNIEXPORT jint JNICALL Java_com_my_control_MyClass_read
  (JNIEnv *, jclass, jint, jbyteArray);

/*
 * Class:     com_my_control_MyClass
 * Method:    write
 * Signature: (I[B)I
 */
JNIEXPORT jint JNICALL Java_com_my_control_MyClass_write
  (JNIEnv *, jclass, jint, jbyteArray);

/*
 * Class:     com_my_control_MyClass
 * Method:    tracename
 * Signature: (II)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_my_control_MyClass_tracename
  (JNIEnv *, jclass, jint, jint);

#ifdef __cplusplus
}
#endif
#endif
