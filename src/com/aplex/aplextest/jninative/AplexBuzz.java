package com.aplex.aplextest.jninative;
/**
 * Created by APLEX on 2016/6/22.
 */
public class AplexBuzz {
    private static final String TAG = "BuzzAplex";

    public int fd;
    /**
     * enable the onboard buzzer.<br>
     * Every time when you set the buzzer frequency, you need to
     * call the enable() function，then it will work under setted frequency
     */
    public static native void enable();
    /**
     * set the onboard buzzer's frequency
     * @param frequency frequency range: 0~20000(Hz)
     */
    public static native void setFrequency(int frequency);
    /**
     * disable the onboard buzzer
     */
    public static native void disable();

    static {
    	
    	System.loadLibrary("aplex");
    	//System.load("/system/lib/libaplex.so");
    }
}
