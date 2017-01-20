package com.aplex.aplextest.jninative;

/**
 * Created by APLEX on 2016/6/22.
 */
public class I2CController {
    public int fd = -1;
    native public int open(String path, int flags);
    native public String readStr(int fd, int addr, int offset, int count);
    native public int writeStr(int fd, int addr, int offset, String buf, int count);
    native public void close(int fd);

    static {
    	System.loadLibrary("aplex");
    	//System.load("/system/lib/libaplex.so");
    }
}
