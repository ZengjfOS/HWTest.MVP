package com.aplex.aplextest.fragment.gpio;

import com.aplex.aplextest.jninative.AplexGPIO;

import android.util.Log;

public class GPIOPresenter extends GPIOContract.Persenter {
	/**
	 * command type
	 */
	public final static int GPIO_IN0_CMD_APLEX = 66;
	public final static int GPIO_IN1_CMD_APLEX = 67;
	public final static int GPIO_IN2_CMD_APLEX = 68;
	public final static int GPIO_IN3_CMD_APLEX = 69;

	public final static int GPIO_OUT0_CMD_APLEX = 70;
	public final static int GPIO_OUT1_CMD_APLEX = 71;
	public final static int GPIO_OUT2_CMD_APLEX = 72;
	public final static int GPIO_OUT3_CMD_APLEX = 73;
	/**
	 * Native接口
	 */
	private AplexGPIO mAplexGPIO;

	@Override
	public void onstart() {
		mAplexGPIO = new AplexGPIO();
	}

	@Override
	public void onDestroy() {
		if (mAplexGPIO != null) {
			mAplexGPIO.close(mAplexGPIO.fd);
		}
		mAplexGPIO = null;
	}

	@Override
	public void setGPIOStatus(int buttonNumber, int status) {
		long inData = -1;
		long result = -1;
		mAplexGPIO.fd = mAplexGPIO.open("/dev/gpio_aplex", 0);
		/**
		 * 检查设备节点是否打开正确，0,1,2被系统占用
		 */
		if (mAplexGPIO.fd > 2) {
			switch (buttonNumber) {
			case 0:
				result = mAplexGPIO.ioctl(mAplexGPIO.fd, GPIO_OUT0_CMD_APLEX, status);
				Log.e("result", result + "");
				break;
			case 1:
				result = mAplexGPIO.ioctl(mAplexGPIO.fd, GPIO_OUT1_CMD_APLEX, status);
				break;
			case 2:
				result = mAplexGPIO.ioctl(mAplexGPIO.fd, GPIO_OUT2_CMD_APLEX, status);
				break;
			case 3:
				result = mAplexGPIO.ioctl(mAplexGPIO.fd, GPIO_OUT3_CMD_APLEX, status);
				break;
			}
			if (result == 0) {
				switch (buttonNumber) {
				case 0:
					inData = mAplexGPIO.ioctl(mAplexGPIO.fd, GPIO_IN0_CMD_APLEX, 0);
					Log.e("Data", inData + "");
					break;
				case 1:
					inData = mAplexGPIO.ioctl(mAplexGPIO.fd, GPIO_IN1_CMD_APLEX, 0);
					break;
				case 2:
					inData = mAplexGPIO.ioctl(mAplexGPIO.fd, GPIO_IN2_CMD_APLEX, 0);
					break;
				case 3:
					inData = mAplexGPIO.ioctl(mAplexGPIO.fd, GPIO_IN3_CMD_APLEX, 0);
					break;
				default:
					break;
				}
			}
		}
		if (mAplexGPIO != null)
			mAplexGPIO.close(mAplexGPIO.fd);
		mView.getInStatus(buttonNumber, inData);

	}

	public boolean checkDevice() {
		return false;
	}

}
