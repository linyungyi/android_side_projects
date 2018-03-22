LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE    := my-jni
LOCAL_SRC_FILES := my-jni.c
LOCAL_CFLAGS    := -Werror
LOCAL_LDLIBS    := -llog
include $(BUILD_SHARED_LIBRARY)
