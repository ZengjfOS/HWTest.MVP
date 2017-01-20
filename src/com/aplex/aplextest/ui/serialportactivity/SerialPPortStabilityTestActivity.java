package com.aplex.aplextest.ui.serialportactivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.util.Date;

import com.aplex.aplextest.Application;
import com.aplex.aplextest.R;
import com.aplex.aplextest.flatui.view.FlatButton;
import com.aplex.aplextest.jninative.SerialPort;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;

public class SerialPPortStabilityTestActivity extends Activity {
	// 设置指定串口
	protected Application mApplication;
	protected SerialPort mSerialPort;
	protected OutputStream mOutputStream;

	private SendMessageTask mMessageTask =  new SendMessageTask();
	private FlatButton mSendButton;
	private boolean mIsSending;
	private class SendMessageTask extends Handler implements Runnable{
		private long num = 0;
		@Override
		public void run() {
			num++;
			sendSerialProtMessage(num+"");
			postDelayed(this, 100);
		}
		public void start(){
			this.postDelayed(this, 100);
		}
		public void stop(){
			//置0;
			num = 0;
			this.removeCallbacks(this);
		}	
	}

	private void sendSerialProtMessage(String num) {
		String s = "1234567890abcdefghijklmnopqrstuvwsyzABCDEFGHIJKLMNOPQRSTUVWSYZ:"+num;
		char[] text = new char[s.length()];
		int i;
		for (i = 0; i < s.length(); i++) {
			text[i] = s.charAt(i);
		}
		try {
			mOutputStream.write(new String(text).getBytes());
			mOutputStream.write("\n\r".getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_serialport_stability_test);
		mApplication = (Application) getApplication();
		
		try {
			mSerialPort = mApplication.getStabilityTestSerialPort();
			mOutputStream = mSerialPort.getOutputStream();
		} catch (SecurityException e) {
			DisplayError(R.string.error_security);
		} catch (IOException e) {
			DisplayError(R.string.error_unknown);
		} catch (InvalidParameterException e) {
			DisplayError(R.string.error_configuration);
		}
		mSendButton = (FlatButton) findViewById(R.id.send_message);
		mSendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mMessageTask == null) {
					mMessageTask = new SendMessageTask();
				}
				if (mIsSending) {
					mIsSending = false;
					mSendButton.setText(R.string.send);
					mMessageTask.stop();
				}else {
					mIsSending = true;
					mSendButton.setText(R.string.sending);
					mMessageTask.start();
					
				}
			}
		});
	}
	private void DisplayError(int errorSecurity) {
		
	}

	@Override
	protected void onPause() {
		mIsSending = false;
		mSendButton.setText(R.string.send);
		mMessageTask.stop();
		super.onPause();
	}
	@Override
	protected void onDestroy() {
		mApplication.closeSerialPort2();
		mSerialPort = null;
		mMessageTask.stop();
		mMessageTask = null;
		super.onDestroy();
	}
}
