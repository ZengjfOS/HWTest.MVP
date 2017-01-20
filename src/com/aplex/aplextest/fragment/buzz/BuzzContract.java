package com.aplex.aplextest.fragment.buzz;

import com.aplex.aplextest.base.BaseModel;
import com.aplex.aplextest.base.BasePresenter;
import com.aplex.aplextest.base.BaseView;


public interface BuzzContract {

	interface Model extends BaseModel{
		
	}
	interface View extends BaseView{
		
	}
	public abstract class Persenter extends BasePresenter<Model, View>{
		public abstract void ChooseHZ();
		@Override
		public void onstart() {
		}
	}
}
