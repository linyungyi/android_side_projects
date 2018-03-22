package com.my.control;

public class MyClass {
	static {
        System.loadLibrary("my-jni");        
    }    
    public static native String hello();   
    public static native int init(String jstr);  
    public static native int ioctl(int code, byte[] buf);   
    public static native int read(int count, byte[] buf);    
    public static native int write(int count, byte[] buf);   
    public static native String tracename(int icModel, int idata);    
}
