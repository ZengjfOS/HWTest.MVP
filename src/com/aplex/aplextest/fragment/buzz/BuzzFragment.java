package com.aplex.aplextest.fragment.buzz;

import java.util.zip.Inflater;

import com.aplex.aplextest.R;
import com.aplex.aplextest.base.BaseFragment;
import com.aplex.aplextest.base.BaseView;
import com.aplex.aplextest.flatui.view.FlatToggleButton;
import com.aplex.aplextest.utils.TUtil;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;


/**
 * Created by APLEX on 2016/6/21.
 */
// 这里主要是看BuzzPresenter、BuzzModel这两个类作为泛型参数传入，在BaseFragment中的TUtil.getT()可以获取到类名，并通过反射创建对应对象；
public class BuzzFragment extends BaseFragment<BuzzPresenter,BuzzModel> implements View.OnClickListener,BaseView {
    public FlatToggleButton mFlatToggleButton;
    public RadioGroup mRadioGroup;
    public LinearLayout RootView;
    public RadioButton mBuzzBtn1;
    public RadioButton mBuzzBtn2;
    public RadioButton mBuzzBtn3;
    public RadioButton mBuzzBtn4;

	@Override
	protected View createView(LayoutInflater inflater) {
        initView(inflater);
        initListener();
		return RootView;
	}
    private void initView(LayoutInflater inflater) {
		// TODO Auto-generated method stub
		RootView = (LinearLayout) inflater.inflate(R.layout.ui_buzz, null);
        mFlatToggleButton = (FlatToggleButton) RootView.findViewById(R.id.buzz_toggle);
        mRadioGroup = (RadioGroup) RootView.findViewById(R.id.buzz_hz);
        mBuzzBtn1 = (RadioButton) RootView.findViewById(R.id.buzz_hz_500);
        mBuzzBtn2 = (RadioButton) RootView.findViewById(R.id.buzz_hz_1000);
        mBuzzBtn3 = (RadioButton) RootView.findViewById(R.id.buzz_hz_1500);
        mBuzzBtn4 = (RadioButton) RootView.findViewById(R.id.buzz_hz_2000);
        RootView.setBackground(getResources().getDrawable(R.color.viewPagerTabBackground));
	}
	private void initListener() {
        mBuzzBtn1.setOnClickListener(this);
        mBuzzBtn2.setOnClickListener(this);
        mBuzzBtn3.setOnClickListener(this);
        mBuzzBtn4.setOnClickListener(this);
        mFlatToggleButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
    	mPresenter.ChooseHZ();
    }
}