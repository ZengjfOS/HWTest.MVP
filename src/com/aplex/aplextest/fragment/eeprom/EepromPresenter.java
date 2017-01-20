package com.aplex.aplextest.fragment.eeprom;

import com.aplex.aplextest.jninative.I2CController;

import android.R.integer;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class EepromPresenter extends EepromContract.Persenter {
	I2CController i2cController;

	@Override
	public void onstart() {
		i2cController = new I2CController();
	}

	@Override
	public void onDestroy() {
		if (i2cController != null) {
			i2cController.close(i2cController.fd);
		}
		Log.e("EepromPresenter", "onDestroy");
		i2cController = null;
	}

	@Override
	public void toGetEepromData(String i2c_node, int writeAddr, int writeOffset, int i) {
		i2cController.fd = i2cController.open(i2c_node, 0);
		String readData = "";
		if (i2cController.fd < 3) {
			mView.showMissPmMsg();
		} else {
			readData = i2cController.readStr(i2cController.fd, writeAddr, writeOffset, 256);
			/**
			 * 这里和-1进行比较是在jni里设定好的，如果出错，那么返回字符串-1
			 */
			if ("-1".equals(readData)) {
				mView.showReadFailedMsg();
			} else {
				mView.showReadSuccessMsg(readData);
			}
		}
		i2cController.close(i2cController.fd);
	}
}
