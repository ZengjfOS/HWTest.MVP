package com.aplex.aplextest.fragment;

import com.aplex.aplextest.R;
import com.aplex.aplextest.flatui.FlatUI;
import com.aplex.aplextest.flatui.view.ColorfulRingProgressView;
import com.aplex.aplextest.flatui.view.FlatButton;
import com.aplex.aplextest.flatui.view.FlatTextView;
import com.aplex.aplextest.ui.stresstestActivity.StressTestDetailActivity;
import com.aplex.aplextest.utils.SPUtils;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

public class StressTestOfHardwareFragment extends Fragment {
	private LinearLayout mRootView;
	private FlatButton mbtn;
	private FlatButton mDetailBtn;
	private Handler handler = new Handler();
	private ColorfulRingProgressView crpv;
	private FlatTextView ftv;
	private ValueAnimator valueAnimator;
	private boolean isFinishText = false;
	private boolean isTesting = false;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		SPUtils.setSPFileName("StressTest");
		SPUtils.setApplication(getActivity().getApplication());
		isFinishText = SPUtils.getBoolean("isFinishText");
		initView(inflater);
		initData();
		initListener();
		FlatUI.initDefaultValues(getActivity());
		FlatUI.setDefaultTheme(FlatUI.SKY);
		mRootView.setBackground(getResources().getDrawable(
				R.color.viewPagerTabBackground));
		return mRootView;
	}

	private void initListener() {
		mbtn.setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {
				if (isTesting) {
					//取消动画
					valueAnimator.cancel();
					//取消测试
					//TODO
					//设置进度条，进度为0
					crpv.setPercent(0);
					ftv.setText("0%");
					isTesting = false;
				}else {
					//开始动画
					valueAnimator.start();
					//开始测试
					//TODO
					isTesting = true;
				}
				//设置按钮文本为Start
				mbtn.setText(isTesting ? getResources().getString(
						R.string.cancel_test) : getResources().getString(
						R.string.start_test));
			}
		});

	}

	private void initData() {
		// TODO Auto-generated method stub

	}

	@SuppressLint("NewApi")
	private void initView(LayoutInflater inflater) {
		mRootView = (LinearLayout) inflater.inflate(R.layout.ui_stresstest,
				null);
		mbtn = (FlatButton) mRootView.findViewById(R.id.test_btn);
		mbtn.setText(getResources().getString(R.string.start_test));
		mDetailBtn = (FlatButton) mRootView.findViewById(R.id.detail_btn);
		mDetailBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (isFinishText) {
					Intent intent = new Intent(getActivity(),
							StressTestDetailActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(
							getActivity(),
							getResources()
									.getString(R.string.please_test_first),
							Toast.LENGTH_LONG).show();
				}

			}
		});
		ftv = (FlatTextView) mRootView.findViewById(R.id.current_percent);

		crpv = (ColorfulRingProgressView) mRootView.findViewById(R.id.crpv);
		crpv.setStartAngle(0);

		valueAnimator = ValueAnimator.ofFloat(0f, 100f);
		valueAnimator.setDuration(15000);

		valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				Float value = (Float) valueAnimator.getAnimatedValue();
				crpv.setPercent(value);
				String currentPercent = String.valueOf(value);
				if (currentPercent.contains(".")) {
					currentPercent = currentPercent.substring(0,
							currentPercent.indexOf('.'));
				}
				ftv.setText(currentPercent + "%");
			}
		});
		valueAnimator.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator arg0) {

			}

			@Override
			public void onAnimationRepeat(Animator arg0) {

			}

			@Override
			public void onAnimationEnd(Animator arg0) {
				isFinishText = true;				
				SPUtils.pushBoolean("isFinishText", isFinishText);
			}

			@Override
			public void onAnimationCancel(Animator arg0) {

			}
		});

	}
}
