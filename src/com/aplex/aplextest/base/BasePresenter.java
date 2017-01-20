package com.aplex.aplextest.base;

import android.content.Context;

public abstract class BasePresenter<M,V>{
	public Context mContext;
	public V mView;
	public M mModel;
	
	public void setVM(V v,M m){
		mView = v;
		mModel = m;
		onstart();
	}
	public abstract void onstart();
	public abstract void onDestroy();
}
