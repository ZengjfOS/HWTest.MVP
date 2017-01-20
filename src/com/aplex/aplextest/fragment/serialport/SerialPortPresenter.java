package com.aplex.aplextest.fragment.serialport;

import com.aplex.aplextest.jninative.COM3Mode;
import com.aplex.aplextest.utils.SPUtils;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;

public class SerialPortPresenter extends SerialPortContract.Presenter {
	private final static String CURRENT_COMMODE = "current_commode";
	SharedPreferences com3mode_sp;

	@Override
	public void onstart() {
		SPUtils.setApplication(((Fragment) mView).getActivity().getApplication());
		// 得出current commode选项
		com3mode_sp = SPUtils.getSP(((Fragment) mView).getActivity().getApplication(), "config");
	}

	@Override
	public void onDestroy() {
	}

	@Override
	public int getComModeCurrentPosition() {
		boolean init = com3mode_sp.getBoolean("init", false);
		if (!init) {
			SPUtils.pushBoolean("init", true);
			SPUtils.pushInt(CURRENT_COMMODE, 0);
		}
		return SPUtils.getInt(CURRENT_COMMODE);
	}

	@Override
	public String[] getComModes() {
		return mModel.getComModes(mContext);
	}

	@Override
	public void setCurrentMode(int which) {
		SPUtils.pushInt(CURRENT_COMMODE, which);
		setComMode(which);
	}

	private void setComMode(int position) {
		switch (position) {
		case 0:
			COM3Mode.setRS232Mode();
			break;
		case 1:
			COM3Mode.setRS485Mode();
			break;
		case 2:
			COM3Mode.setRS422Mode();
			break;
		case 3:
			COM3Mode.setLoopBackMode();
			break;
		}
	}

	@Override
	public String[] getSerialPortOptDatas() {
		return mModel.getSerialPortOptDatas(mContext);
	}

	@Override
	public void performClickEvent(int position) {
		performByPosition(position);
	}

	private void performByPosition(int position) {
		String[] serialPortOptDatas = mModel.getSerialPortOptDatas(mContext);
		if (serialPortOptDatas[position].equals(mModel.getComModeString(mContext)));
			mView.performComMode();
		if (serialPortOptDatas[position].equals(mModel.getSetupString(mContext)));
			mView.performSetup();
		if (serialPortOptDatas[position].equals(mModel.getConsoleString(mContext)));
			mView.performConsole();
		if (serialPortOptDatas[position].equals(mModel.getLoopBackString(mContext)));
			mView.performLoopback();
		if (serialPortOptDatas[position].equals(mModel.getStabilityString(mContext))) {
			mView.performStability();
		}
	}
}
