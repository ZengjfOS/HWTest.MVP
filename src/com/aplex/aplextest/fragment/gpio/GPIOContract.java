package com.aplex.aplextest.fragment.gpio;

import com.aplex.aplextest.base.BaseModel;
import com.aplex.aplextest.base.BasePresenter;
import com.aplex.aplextest.base.BaseView;

public interface GPIOContract {

	interface Model extends BaseModel {

	}

	interface View extends BaseView {

		void getInStatus(int buttonNumber, long inData);

	}

	public abstract class Persenter extends BasePresenter<Model, View> {

		public abstract void setGPIOStatus(int buttonNumber, int i);

		public abstract boolean checkDevice();
	}
}
