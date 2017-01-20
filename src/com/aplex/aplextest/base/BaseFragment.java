package com.aplex.aplextest.base;

import com.aplex.aplextest.utils.TUtil;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment<P extends BasePresenter, M extends BaseModel> extends Fragment{
	public P mPresenter;
	public M mModel;
	public Context mContext;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mPresenter = TUtil.getT(this, 0);
		mModel = TUtil.getT(this, 1);
	
		if (this instanceof BaseView)
			mPresenter.setVM(this, mModel);
		return createView(inflater);
	}

	protected abstract View createView(LayoutInflater inflater);

	@Override
	public void onDestroy() {
		mPresenter.onDestroy();
		super.onDestroy();
	}
}
