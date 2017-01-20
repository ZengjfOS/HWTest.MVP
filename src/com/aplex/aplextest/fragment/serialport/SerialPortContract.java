package com.aplex.aplextest.fragment.serialport;

import com.aplex.aplextest.base.BaseModel;
import com.aplex.aplextest.base.BasePresenter;
import com.aplex.aplextest.base.BaseView;

import android.content.Context;

public interface SerialPortContract {
	public interface Model extends BaseModel {

		String[] getSerialPortOptDatas(Context mContext);

		String[] getComModes(Context mContext);

		String getComModeString(Context mContext);

		String getSetupString(Context context);
		
		String getConsoleString(Context context);
		
		String getLoopBackString(Context context);
		
		String getStabilityString(Context context);


	}

	public interface View extends BaseView {

		void performComMode();

		void performSetup();

		void performConsole();

		void performLoopback();

		void performStability();

		

	}

	public abstract class Presenter extends BasePresenter<Model, View> {
		public abstract void setCurrentMode(int which);

		public abstract String[] getComModes();
		
		public abstract int  getComModeCurrentPosition();
		
		public abstract String[] getSerialPortOptDatas();

		public void performClickEvent(int position) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
