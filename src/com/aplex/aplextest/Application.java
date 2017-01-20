package com.aplex.aplextest;

/**
 * Created by APLEX on 2016/6/23.
 */
/*
 * Copyright 2009 Cedric Priscal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.SharedPreferences;
import android.util.Log;

import com.aplex.aplextest.flatui.FlatUI;
import com.aplex.aplextest.jninative.SerialPort;
import com.aplex.aplextest.ui.serialportactivity.SerialPortFinder;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;

public class Application extends android.app.Application {
	@Override
	public void onCreate() {
		FlatUI.initDefaultValues(this);
		FlatUI.setDefaultTheme(FlatUI.SKY);
		super.onCreate();
	}

	// 找出一个
	public SerialPortFinder mSerialPortFinder = new SerialPortFinder();
	private SerialPort mSerialPort = null;
	private SerialPort mSerialPort2 = null;

	public SerialPort getSerialPort() throws SecurityException, IOException,
			InvalidParameterException {
		if (mSerialPort == null) {
			/* Read serial port parameters */
			SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
			String path = sp.getString("DEVICE", "");
			int baudrate = Integer.decode(sp.getString("BAUDRATE", "-1"));
			/* Check parameters */
			if ((path.length() == 0) || (baudrate == -1)) {
				throw new InvalidParameterException();
			}
			Log.e("Application", path);
			Log.e("Application", baudrate + "");
			/* Open the serial port */
			mSerialPort = new SerialPort(new File(path), baudrate, 0);
		}
		return mSerialPort;
	}

	public SerialPort getStabilityTestSerialPort() throws SecurityException,
			IOException, InvalidParameterException {
		if (mSerialPort == null) {
			/* Read serial port parameters */
			// SharedPreferences sp = getSharedPreferences("config",
			// MODE_PRIVATE);
			String path = "/dev/ttymxc2";
			int baudrate = 9600;
			/* Check parameters */
			if ((path.length() == 0) || (baudrate == -1)) {
				throw new InvalidParameterException();
			}
			Log.e("Application", path);
			Log.e("Application", baudrate + "");
			/* Open the serial port */
			mSerialPort2 = new SerialPort(new File(path), baudrate, 0);
		}
		return mSerialPort2;
	}

	public void closeSerialPort() {
		if (mSerialPort != null) {
			try {
				mSerialPort.getInputStream().close();
				mSerialPort.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			mSerialPort.close();
			mSerialPort = null;
		}
	}

	public void closeSerialPort2() {
		if (mSerialPort2 != null) {
			mSerialPort2.close();
			mSerialPort2 = null;
		}
	}
}
