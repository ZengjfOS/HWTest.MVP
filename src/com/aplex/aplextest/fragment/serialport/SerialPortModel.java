package com.aplex.aplextest.fragment.serialport;

import com.aplex.aplextest.R;

import android.content.Context;

public class SerialPortModel implements SerialPortContract.Model{

	@Override
	public String[] getSerialPortOptDatas(Context mContext) {
		return mContext.getResources().getStringArray(R.array.serialport_opt);
	}


	@Override
	public String[] getComModes(Context mContext) {
		return mContext.getResources().getStringArray(R.array.com_mode_opt);
	}


	@Override
	public String getComModeString(Context mContext) {
		return mContext.getResources().getString(R.string.serialport_opt_of_com_mode_set);
	}


	@Override
	public String getSetupString(Context context) {
		return context.getResources().getString(R.string.serialport_opt_of_setup);
	}

	@Override
	public String getConsoleString(Context context) {
		return context.getResources().getString(R.string.serialport_opt_of_console);
	}

	@Override
	public String getLoopBackString(Context context) {
		return context.getResources().getString(R.string.serialport_opt_of_loopback);
	}

	@Override
	public String getStabilityString(Context context) {
		return context.getResources().getString(R.string.serialport_opt_of_Stability);
	}

}
