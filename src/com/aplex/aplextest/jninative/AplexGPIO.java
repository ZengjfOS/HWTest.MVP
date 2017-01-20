package com.aplex.aplextest.jninative;

/**
 * Created by APLEX on 2016/6/22.
 */
public class AplexGPIO {
    public int fd = 0;

    public native int open(String path, int flags);

    public native void close(int fd);

    public native long ioctl(int fd, int cmd, int value);

    static {
    	System.loadLibrary("aplex");
       //System.load("/system/lib/libaplex.so");
    }
}
