package com.aplex.aplextest.fragment.gpio;

import java.io.File;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aplex.aplextest.R;
import com.aplex.aplextest.base.BaseFragment;
import com.aplex.aplextest.flatui.FlatUI;
import com.aplex.aplextest.flatui.view.FlatButton;
import com.aplex.aplextest.jninative.AplexGPIO;

/**
 * Created by APLEX on 2016/6/22.
 */
public class GPIOFragment extends BaseFragment<GPIOPresenter, GPIOModel> implements GPIOContract.View {

	/**
	 * 输入按钮
	 */
	private FlatButton mInBtn0;
	private FlatButton mInBtn1;
	private FlatButton mInBtn2;
	private FlatButton mInBtn3;

	/**
	 * 输出按钮
	 */
	private FlatButton mOutBtn0;
	private FlatButton mOutBtn1;
	private FlatButton mOutBtn2;
	private FlatButton mOutBtn3;

	private LinearLayout mRootView;
	/**
	 * Native接口
	 */

	@Override
	protected View createView(LayoutInflater inflater) {
		initview(inflater);
		initdata();
		initlistener();
		return mRootView;
	}

	private void initlistener() {
		/**
		 * 检查设备节点是否存在
		 */
		File file = new File("dev/gpio_aplex");
		if (file.exists()) {
			/**
			 * 检查设备节点是否打开正确，0,1,2被系统占用
			 */

			GPIOOnClick gpioOnClick = new GPIOOnClick();
			mOutBtn0.setOnClickListener(gpioOnClick);
			mOutBtn1.setOnClickListener(gpioOnClick);
			mOutBtn2.setOnClickListener(gpioOnClick);
			mOutBtn3.setOnClickListener(gpioOnClick);
			/**
			 * 开启定时器调度，主要用于更新输入按钮的界面
			 */
		} else {
			/**
			 * 文件节点可能不存在
			 */
			Toast.makeText(getActivity(), "Can't find driver node file.", Toast.LENGTH_SHORT).show();
		}
	}

	private void initdata() {
	}

	private void initview(LayoutInflater inflater) {
		mRootView = (LinearLayout) inflater.inflate(R.layout.ui_gpio, null);
		mOutBtn0 = (FlatButton) mRootView.findViewById(R.id.gpio_out_0);
		mOutBtn1 = (FlatButton) mRootView.findViewById(R.id.gpio_out_1);
		mOutBtn2 = (FlatButton) mRootView.findViewById(R.id.gpio_out_2);
		mOutBtn3 = (FlatButton) mRootView.findViewById(R.id.gpio_out_3);

		mInBtn0 = (FlatButton) mRootView.findViewById(R.id.gpio_in_0);
		mInBtn1 = (FlatButton) mRootView.findViewById(R.id.gpio_in_1);
		mInBtn2 = (FlatButton) mRootView.findViewById(R.id.gpio_in_2);
		mInBtn3 = (FlatButton) mRootView.findViewById(R.id.gpio_in_3);
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	class GPIOOnClick implements View.OnClickListener {
		@Override
		public void onClick(View arg0) {
			/**
			 * 获取当前按下的按钮
			 */
			Button currentButton = (Button) arg0;
			/**
			 * 获取按钮上的文字
			 */
			String text = currentButton.getText().toString();
			/**
			 * 获取按钮号，主要是因为，按钮文字里面正好有相应的按钮号
			 */
			int buttonNumber = Integer.valueOf("" + text.charAt(6));
			/**
			 * 根据按钮上的文字最后的字符来判断当前按钮的状态,并做对应的事情
			 */

			if (text.endsWith("_H")) {
				currentButton.setText(text.replace('H', 'L'));
				/**
				 * 设置状态1，也就是高电平
				 */
				mPresenter.setGPIOStatus(buttonNumber, 1);
			} else {
				currentButton.setText(text.replace('L', 'H'));
				/**
				 * 设置状态0，也就是低电平
				 */
				mPresenter.setGPIOStatus(buttonNumber, 0);
			}
		}
	}

	@Override
	public void getInStatus(int buttonNumber, long inData) {
		String text = "";
		switch (buttonNumber) {
		case 0:
			text = mInBtn0.getText().toString();
			mInBtn0.setText(text.substring(0, text.length() - 1) + (inData != 0 ? "H" : "L"));
			break;
		case 1:
			text = mInBtn1.getText().toString();
			mInBtn1.setText(text.substring(0, text.length() - 1) + (inData != 0 ? "H" : "L"));
			break;
		case 2:
			text = mInBtn2.getText().toString();
			mInBtn2.setText(text.substring(0, text.length() - 1) + (inData != 0 ? "H" : "L"));
			break;
		case 3:
			text = mInBtn3.getText().toString();
			mInBtn3.setText(text.substring(0, text.length() - 1) + (inData != 0 ? "H" : "L"));
			break;
		}
	}
}
