package com.aplex.aplextest.fragment.eeprom;

import com.aplex.aplextest.base.BaseModel;
import com.aplex.aplextest.base.BasePresenter;
import com.aplex.aplextest.base.BaseView;

public interface EepromContract {

	interface Model extends BaseModel {
		String[] getDeviceNodes();
	}

	interface View extends BaseView {

		void showMissPmMsg();

		void showReadFailedMsg();

		void showReadSuccessMsg(String readData);
	}

	public abstract class Persenter extends BasePresenter<Model, View> {

		public abstract void toGetEepromData(String i2c_node, int writeAddr, int writeOffset, int i);

		@Override
		public void onstart() {

		}
	}
}
