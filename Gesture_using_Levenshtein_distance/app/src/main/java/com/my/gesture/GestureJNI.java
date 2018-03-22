package com.my.gesture;

/**
 * Created by linyu on 2016/6/13.
 */
public class GestureJNI {
    static {
        System.loadLibrary("zet-gesture-jni");
    }
    public static native String hello();
    public static native int editDistance(int lengthTarget,int lengthSource, byte[] bufTarget, byte[] bufSource);
}
