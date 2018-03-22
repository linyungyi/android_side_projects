LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE    := zet-gesture-jni
LOCAL_SRC_FILES := GestureJNI.c
LOCAL_CFLAGS    := -Werror
LOCAL_LDLIBS    := -llog
include $(BUILD_SHARED_LIBRARY)
