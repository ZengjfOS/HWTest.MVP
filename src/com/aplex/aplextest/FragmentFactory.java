package com.aplex.aplextest;

import com.aplex.aplextest.fragment.AboutFragment;
import com.aplex.aplextest.fragment.EthernetAutoConfFragment;
import com.aplex.aplextest.fragment.SerialPortToolFragment;
import com.aplex.aplextest.fragment.buzz.BuzzFragment;
import com.aplex.aplextest.fragment.eeprom.EepromFragment;
import com.aplex.aplextest.fragment.gpio.GPIOFragment;

import android.support.v4.app.Fragment;
import android.support.v4.util.SparseArrayCompat;

/**
 * Created by APLEX on 2016/6/27.
 */
public class FragmentFactory {
	// Fragmet 缓存区，防止多次创建
    private static SparseArrayCompat<Fragment> mCaches = new SparseArrayCompat<Fragment>();

    public static Fragment getFragment(int position) {
    	// 当对应的Fragment不存在的时候就创建，如果有了，就直接返回。

        Fragment fragment = mCaches.get(position);
        if (fragment != null) {
            return fragment;
        }

        switch (position)
        {
            case 0:
                // buzz
                fragment = new BuzzFragment();
                break;
            case 1:
                // eeprom
                fragment = new EepromFragment();
                break;
            case 2:
                // gpio
                fragment = new GPIOFragment();
                break;
            case 3:
                // SerialPort
                fragment = new SerialPortToolFragment();
                break;
            case 4:
                // Ethernet
                fragment = new EthernetAutoConfFragment();
                break;
            case 5:
                // StressTest
                fragment = new AboutFragment();
                break;
            default:
                break;
        }

        // 存储
        mCaches.put(position, fragment);

        return fragment;
    }

}
