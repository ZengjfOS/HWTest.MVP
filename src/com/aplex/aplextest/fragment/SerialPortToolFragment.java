package com.aplex.aplextest.fragment;

import java.util.Arrays;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.aplex.aplextest.R;
import com.aplex.aplextest.flatui.FlatUI;
import com.aplex.aplextest.flatui.view.FlatTextView;
import com.aplex.aplextest.jninative.COM3Mode;
import com.aplex.aplextest.ui.serialportactivity.ConsoleActivity;
import com.aplex.aplextest.ui.serialportactivity.LoopbackActivity;
import com.aplex.aplextest.ui.serialportactivity.Sending01010101Activity;
import com.aplex.aplextest.ui.serialportactivity.SerialPPortStabilityTestActivity;
import com.aplex.aplextest.ui.serialportactivity.SerialPortPreferences;
import com.aplex.aplextest.utils.SPUtils;

/**
 * Created by APLEX on 2016/6/22.
 */
public class SerialPortToolFragment extends Fragment implements
		AdapterView.OnItemClickListener {
	private LinearLayout mRootView;
	private ListView mSerialPortToolListView;
	private String[] mSerialPortOptDatas;
	private SharedPreferences com3mode_sp;
	private int mCurrentComModePosition;
	private String[] COMModes;
	private List<String> COMModes_list;
	private final static String CURRENT_COMMODE = "current_commode";

	// com mode opt
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		FlatUI.initDefaultValues(getActivity());
		FlatUI.setDefaultTheme(FlatUI.SKY);
		initView(inflater);
		initData();
		initListener();
		mRootView.setBackground(getResources().getDrawable(
				R.color.viewPagerTabBackground));
		return mRootView;
	}

	private void initData() {
		COMModes = getResources().getStringArray(R.array.com_mode_opt);
		COMModes_list = Arrays.asList(COMModes);
		SPUtils.setApplication(getActivity().getApplication());
		com3mode_sp = SPUtils.getSP(getActivity().getApplication(), "config");
		boolean init = com3mode_sp.getBoolean("init", false);
		if (!init) {
			SPUtils.pushBoolean("init", true);
			SPUtils.pushInt(CURRENT_COMMODE, 0);
		}
		mCurrentComModePosition = SPUtils.getInt(CURRENT_COMMODE);
	}

	private void initView(LayoutInflater inflater) {
		mRootView = (LinearLayout) inflater.inflate(R.layout.ui_serialporttool,
				null);
		mSerialPortToolListView = (ListView) mRootView
				.findViewById(R.id.serialporttool_list);
		mSerialPortOptDatas = getResources().getStringArray(
				R.array.serialport_opt);
		mSerialPortToolListView.setAdapter(new SerialPortToolAdapter());

	}

	private void initListener() {
		mSerialPortToolListView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		performByPosition(position);
		// 进入COM Mode Opt
	}

	private void performByPosition(int position) {
		if (mSerialPortOptDatas[position].equals(getResources().getString(
				R.string.serialport_opt_of_com_mode_set)))
			selectComMode();
		if (mSerialPortOptDatas[position].equals(getResources().getString(
				R.string.serialport_opt_of_setup)))
			performSetup();
		if (mSerialPortOptDatas[position].equals(getResources().getString(
				R.string.serialport_opt_of_console)))
			performConsole();
		if (mSerialPortOptDatas[position].equals(getResources().getString(
				R.string.serialport_opt_of_loopback)))
			performLoopback();
		if (mSerialPortOptDatas[position].equals(getResources().getString(
				R.string.serialport_opt_of_Stability))) {
			performStability();
		}
		// if
		// (mSerialPortOptDatas[position].equals(getResources().getString(R.string.serialport_opt_of_send01010101)))
		// performSend0101();
	}

	private void performStability() {
		Intent intent = new Intent(getActivity(), SerialPPortStabilityTestActivity.class);
		startActivity(intent);
	}

	private void performSend0101() {
		Intent intent = new Intent(getActivity(), Sending01010101Activity.class);
		startActivity(intent);
	}

	private void performLoopback() {
		Intent intent = new Intent(getActivity(), LoopbackActivity.class);
		startActivity(intent);
	}

	private void performConsole() {
		Intent intent = new Intent(getActivity(), ConsoleActivity.class);
		startActivity(intent);
	}

	private void performSetup() {
		Intent intent = new Intent(getActivity(), SerialPortPreferences.class);
		startActivity(intent);
	}
	AlertDialog mAlertDialog ;
	private void selectComMode() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(
				getActivity());
		
		builder.setTitle(getResources().getString(R.string.dialog_title));
		builder.setSingleChoiceItems(COMModes, mCurrentComModePosition,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mCurrentComModePosition = which;
						SPUtils.pushInt(CURRENT_COMMODE, which);
						setComMode(which);
						if (mAlertDialog != null) {
							mAlertDialog.dismiss();
						}
					}
				});
		builder.setCancelable(true);
		mAlertDialog = builder.show();
	}

	private void setComMode(int position) {
		switch (position) {
		case 0:
			COM3Mode.setRS232Mode();
			break;
		case 1:
			COM3Mode.setRS485Mode();
			;
			break;
		case 2:
			COM3Mode.setRS422Mode();
			break;
		case 3:
			COM3Mode.setLoopBackMode();
			break;
		}
	}

	private class SerialPortToolAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return mSerialPortOptDatas.length;
		}

		@Override
		public Object getItem(int position) {
			return mSerialPortOptDatas[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(getActivity(),
						R.layout.item_ui_serialport_opt, null);
				holder = new ViewHolder();
				holder.title = (FlatTextView) convertView
						.findViewById(R.id.serialporttool_opt);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.title.setText(mSerialPortOptDatas[position]);
			return convertView;
		}
	}

	static class ViewHolder {
		FlatTextView title;
	}
}
