package com.aplex.aplextest.fragment.serialport;

import com.aplex.aplextest.R;
import com.aplex.aplextest.base.BaseFragment;
import com.aplex.aplextest.flatui.view.FlatTextView;
import com.aplex.aplextest.ui.serialportactivity.ConsoleActivity;
import com.aplex.aplextest.ui.serialportactivity.LoopbackActivity;
import com.aplex.aplextest.ui.serialportactivity.SerialPPortStabilityTestActivity;
import com.aplex.aplextest.ui.serialportactivity.SerialPortPreferences;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Created by APLEX on 2016/6/22.
 */
public class SerialPortToolFragment extends BaseFragment<SerialPortPresenter, SerialPortModel>
		implements AdapterView.OnItemClickListener, SerialPortContract.View {
	private LinearLayout mRootView;
	private ListView mSerialPortToolListView;
	private String[] mSerialPortOptDatas;
	private int mCurrentComModePosition;
	private String[] COMModes;

	@Override
	protected View createView(LayoutInflater inflater) {
		initView(inflater);
		initData();
		initListener();
		mRootView.setBackground(getResources().getDrawable(R.color.viewPagerTabBackground));
		return mRootView;
	}

	private void initData() {
		COMModes = mPresenter.getComModes();
		mCurrentComModePosition = mPresenter.getComModeCurrentPosition();
	}

	private void initView(LayoutInflater inflater) {
		mRootView = (LinearLayout) inflater.inflate(R.layout.ui_serialporttool, null);
		mSerialPortToolListView = (ListView) mRootView.findViewById(R.id.serialporttool_list);

		// 获得列表数据
		mSerialPortOptDatas = getResources().getStringArray(R.array.serialport_opt);
		mSerialPortToolListView.setAdapter(new SerialPortToolAdapter());
	}

	private void initListener() {
		mSerialPortToolListView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		mPresenter.performClickEvent(position);
	}

	@Override
	public void performStability() {
		Intent intent = new Intent(getActivity(), SerialPPortStabilityTestActivity.class);
		startActivity(intent);
	}

	@Override
	public void performLoopback() {
		Intent intent = new Intent(getActivity(), LoopbackActivity.class);
		startActivity(intent);
	}

	@Override
	public void performConsole() {
		Intent intent = new Intent(getActivity(), ConsoleActivity.class);
		startActivity(intent);
	}

	@Override
	public void performSetup() {
		Intent intent = new Intent(getActivity(), SerialPortPreferences.class);
		startActivity(intent);
	}

	AlertDialog mAlertDialog;

	@Override
	public void performComMode() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setTitle(getResources().getString(R.string.dialog_title));
		builder.setSingleChoiceItems(COMModes, mCurrentComModePosition, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mCurrentComModePosition = which;
				mPresenter.setCurrentMode(which);
				if (mAlertDialog != null) {
					mAlertDialog.dismiss();
				}
			}
		});
		builder.setCancelable(true);
		mAlertDialog = builder.show();
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
				convertView = View.inflate(getActivity(), R.layout.item_ui_serialport_opt, null);
				holder = new ViewHolder();
				holder.title = (FlatTextView) convertView.findViewById(R.id.serialporttool_opt);
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
