package com.aplex.aplextest.jninative;

/**
 * Created by APLEX on 2016/6/23.
 */
public class COM3Mode {

    static public native void setRS232Mode();
    static public native void setRS485Mode();
    static public native void setRS422Mode();
    static public native void setLoopBackMode();

    static {
    	System.loadLibrary("aplex");
    	//System.load("/system/lib/libaplex.so");
    }
}
