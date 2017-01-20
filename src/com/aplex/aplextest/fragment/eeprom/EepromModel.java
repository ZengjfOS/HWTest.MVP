package com.aplex.aplextest.fragment.eeprom;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

public class EepromModel implements EepromContract.Model{

	@Override
	public String[] getDeviceNodes() {
        ArrayList<String> mDevices = new ArrayList<String>();
        File dev = new File("/dev");
        //File[] files = dev.listFiles();
        File[] files = dev.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.getAbsolutePath().startsWith("/dev/i2c-")) {
                    return true;
                }
                return false;
            }
        });
        int i;
        /**
         * 这里使用这种方式加入节点名，是因为只有这样加，spinner中节点显示才能显示正确的顺序
         */
        for (i=files.length-1; i >= 0; i--) {
            mDevices.add(files[i].getAbsolutePath());
        }
        return (String[])mDevices.toArray(new String[mDevices.size()]);
	}

}
