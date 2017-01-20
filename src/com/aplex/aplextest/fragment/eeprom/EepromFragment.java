package com.aplex.aplextest.fragment.eeprom;

import com.aplex.aplextest.R;
import com.aplex.aplextest.base.BaseFragment;
import com.aplex.aplextest.flatui.view.FlatButton;
import com.aplex.aplextest.flatui.view.FlatEditText;
import com.aplex.aplextest.jninative.I2CController;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by APLEX on 2016/6/22.
 */
public class EepromFragment extends BaseFragment<EepromPresenter, EepromModel> implements EepromContract.View {
	private static final String TAG = "EepromFragment";
	public LinearLayout mRootView;
	public FlatEditText mAddressBtn;
	public FlatEditText mAddressOffsetBtn;
	// private FlatButton mWriteBtn;
	public FlatButton mReadBtn;
	// private FlatEditText mWriteEdt;
	public FlatEditText mReadEdt;
	public Spinner mSpinner;
	public String i2c_node = "/dev/i2c-0";
	I2CController i2cController = null;

	@Override
	protected View createView(LayoutInflater inflater) {
		initView(inflater);
		initListener();
		return mRootView;
	}

	private void initListener() {
		mReadBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int writeAddr = Integer.decode("0x" + mAddressBtn.getText().toString());
				int writeOffset = Integer.decode("0x" + mAddressOffsetBtn.getText().toString());
				mPresenter.toGetEepromData(i2c_node, writeAddr, writeOffset, 256);
			}
		});
		mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				/**
				 * 获取节点文件路径
				 */
				// 防止空指针
				if (arg1 != null) {
					i2c_node = ((TextView) arg1).getText().toString();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	private void initView(LayoutInflater inflater) {
		mRootView = (LinearLayout) inflater.inflate(R.layout.ui_eeprom, null);
		// init spinner
		mSpinner = (Spinner) mRootView.findViewById(R.id.themes_spinner);
		Log.e(TAG, mModel.getDeviceNodes().toString());
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_button,
				mModel.getDeviceNodes());
		;
		adapter.setDropDownViewResource(R.layout.simple_flat_list_item);
		mSpinner.setAdapter(adapter);

		// init button
		mAddressBtn = (FlatEditText) mRootView.findViewById(R.id.address_btn);
		mAddressOffsetBtn = (FlatEditText) mRootView.findViewById(R.id.address_offset_btn);
		// mWriteBtn = (FlatButton)
		// mRootView.findViewById(R.id.write_message_btn);
		mReadBtn = (FlatButton) mRootView.findViewById(R.id.read_message_btn);
		mAddressBtn.setText("50");
		mAddressOffsetBtn.setText("00");

		// mWriteEdt = (FlatEditText)
		// mRootView.findViewById(R.id.write_message);
		mReadEdt = (FlatEditText) mRootView.findViewById(R.id.read_message);
		mRootView.setBackground(getResources().getDrawable(R.color.viewPagerTabBackground));
	}

	@Override
	public void showMissPmMsg() {
		Toast.makeText(getActivity(), "Missing read/write permission, \ntrying to chmod the file.", Toast.LENGTH_LONG)
				.show();
	}

	@Override
	public void showReadFailedMsg() {
		Toast.makeText(getActivity(), "Read failed.", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void showReadSuccessMsg(String readData) {
		mReadEdt.setText(readData);
		Toast.makeText(getActivity(), "Read successed.", Toast.LENGTH_SHORT).show();

	}
}
