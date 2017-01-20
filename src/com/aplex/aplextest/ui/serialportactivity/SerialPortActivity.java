package com.aplex.aplextest.ui.serialportactivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.SystemClock;

import com.aplex.aplextest.Application;
import com.aplex.aplextest.R;
import com.aplex.aplextest.jninative.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

/**
 * Created by APLEX on 2016/6/23.
 */
public abstract class SerialPortActivity extends Activity {

	protected Application mApplication;
	protected SerialPort mSerialPort;
	protected OutputStream mOutputStream;
	private InputStream mInputStream;
	private ReadThread mReadThread;
	private boolean openFlag = true;

	private class ReadThread extends Thread {
		@Override
		public void run() {
			while (openFlag) {
				int size;
				try {
					byte[] buffer = new byte[64];
					if (mInputStream == null)
						return;
					if (mInputStream.available() > 0) {
						size = mInputStream.read(buffer);
						if (size > 0) {
							onDataReceived(buffer, size);
						}
					}
					SystemClock.sleep(100);
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}

	private void DisplayError(int resourceId) {
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setTitle("Error");
		b.setMessage(resourceId);
		b.setPositiveButton("OK", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				try {
					if (mOutputStream != null) {
						mOutputStream.close();
					}
					if (mInputStream != null) {
						mInputStream.close();
					}
				} catch (IOException e) {

					e.printStackTrace();
				}

				mApplication.closeSerialPort();
				mSerialPort = null;

				SerialPortActivity.this.finish();
			}
		});
		b.show();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApplication = (Application) getApplication();
		try {
			mSerialPort = mApplication.getSerialPort();
			mOutputStream = mSerialPort.getOutputStream();
			mInputStream = mSerialPort.getInputStream();
			if (mOutputStream == null) {

			}
			/* Create a receiving thread */
			mReadThread = new ReadThread();
			mReadThread.start();
		} catch (NullPointerException e) {
			DisplayError(R.string.error_unknown);
		} catch (SecurityException e) {
			DisplayError(R.string.error_security);
		} catch (IOException e) {
			DisplayError(R.string.error_unknown);
		} catch (InvalidParameterException e) {
			DisplayError(R.string.error_configuration);
		}
	}

	protected abstract void onDataReceived(final byte[] buffer, final int size);

	@Override
	protected void onDestroy() {
		mSerialPort = null;
		mApplication.closeSerialPort();
		try {
			if (mOutputStream != null) {
				mOutputStream.close();
			}
			if (mInputStream != null) {
				mInputStream.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		openFlag = false;
		super.onDestroy();
	}
}
