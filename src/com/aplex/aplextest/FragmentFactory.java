package com.aplex.aplextest;

import android.support.v4.app.Fragment;
import android.support.v4.util.SparseArrayCompat;

import com.aplex.aplextest.fragment.AboutFragment;
import com.aplex.aplextest.fragment.BuzzFragment;
import com.aplex.aplextest.fragment.EepromFragment;
import com.aplex.aplextest.fragment.EthernetAutoConfFragment;
import com.aplex.aplextest.fragment.GPIOFragment;
import com.aplex.aplextest.fragment.SerialPortToolFragment;
import com.aplex.aplextest.fragment.StabilityTestFragment;
import com.aplex.aplextest.fragment.StressTestOfHardwareFragment;
import com.aplex.aplextest.jninative.StabilityTest;

/**
 * Created by APLEX on 2016/6/27.
 */
public class FragmentFactory {
    private static SparseArrayCompat<Fragment> mCaches = new SparseArrayCompat<Fragment>();

    public static Fragment getFragment(int position) {
        Fragment fragment = mCaches.get(position);

        //
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
